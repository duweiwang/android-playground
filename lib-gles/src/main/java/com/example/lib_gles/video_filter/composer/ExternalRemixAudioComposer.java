package com.example.lib_gles.video_filter.composer;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;

class ExternalRemixAudioComposer implements IAudioComposer {
    private static final MuxRender.SampleType SAMPLE_TYPE = MuxRender.SampleType.AUDIO;

    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final int DRAIN_STATE_CONSUMED = 2;

    private final MediaExtractor extractor = new MediaExtractor();
    private final MuxRender muxer;
    private final long targetDurationUs;
    private final int audioBitrate;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

    private int trackIndex;
    private MediaFormat inputFormat;
    private MediaFormat outputFormat;

    private MediaCodec decoder;
    private MediaCodec encoder;
    private MediaFormat actualOutputFormat;

    private MediaCodecBufferCompatWrapper decoderBuffers;
    private MediaCodecBufferCompatWrapper encoderBuffers;

    private boolean isExtractorEOS;
    private boolean isDecoderEOS;
    private boolean isEncoderEOS;
    private boolean decoderStarted;
    private boolean encoderStarted;

    private AudioChannel audioChannel;
    private long writtenPresentationTimeUs;
    private long loopOffsetUs;
    private long lastSampleTimeUs;
    private long prevSampleTimeUs;
    private long cachedDurationUs = -1;

    ExternalRemixAudioComposer(String audioPath, MuxRender muxer, long targetDurationUs, int audioBitrate) throws IOException {
        this.muxer = muxer;
        this.targetDurationUs = targetDurationUs;
        this.audioBitrate = audioBitrate;
        extractor.setDataSource(audioPath);
        trackIndex = selectAudioTrack(extractor);
        if (trackIndex < 0) {
            throw new ComposerException(ErrorCode.NO_AUDIO_TRACK, "No audio track found in external audio.");
        }
        inputFormat = extractor.getTrackFormat(trackIndex);
    }

    @Override
    public void setup() {
        extractor.selectTrack(trackIndex);

        int sampleRate = getFormatInt(inputFormat, MediaFormat.KEY_SAMPLE_RATE, 44100);
        int channelCount = getFormatInt(inputFormat, MediaFormat.KEY_CHANNEL_COUNT, 2);

        outputFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", sampleRate, channelCount);
        outputFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, audioBitrate);
        outputFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16 * 1024);

        try {
            encoder = MediaCodec.createEncoderByType(outputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new ComposerException(ErrorCode.CODEC_INIT, e);
        }
        encoder.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoder.start();
        encoderStarted = true;
        encoderBuffers = new MediaCodecBufferCompatWrapper(encoder);

        String inputMime = inputFormat.getString(MediaFormat.KEY_MIME);
        if (inputMime == null || !inputMime.startsWith("audio/")) {
            throw new ComposerException(ErrorCode.INVALID_AUDIO_MIME, "Invalid external audio mime type.");
        }
        try {
            decoder = MediaCodec.createDecoderByType(inputMime);
        } catch (IOException e) {
            throw new ComposerException(ErrorCode.CODEC_INIT, e);
        }
        decoder.configure(inputFormat, null, null, 0);
        decoder.start();
        decoderStarted = true;
        decoderBuffers = new MediaCodecBufferCompatWrapper(decoder);

        audioChannel = new AudioChannel(decoder, encoder, outputFormat);
    }

    @Override
    public boolean stepPipeline() {
        boolean busy = false;

        int status;
        while (drainEncoder(0) != DRAIN_STATE_NONE) busy = true;
        do {
            status = drainDecoder(0);
            if (status != DRAIN_STATE_NONE) busy = true;
        } while (status == DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY);

        while (audioChannel.feedEncoder(0)) busy = true;
        while (drainExtractor(0) != DRAIN_STATE_NONE) busy = true;

        return busy;
    }

    private int drainExtractor(long timeoutUs) {
        if (isExtractorEOS) return DRAIN_STATE_NONE;

        int currentTrack = extractor.getSampleTrackIndex();
        if (currentTrack >= 0 && currentTrack != trackIndex) {
            return DRAIN_STATE_NONE;
        }

        int result = decoder.dequeueInputBuffer(timeoutUs);
        if (result < 0) return DRAIN_STATE_NONE;

        if (currentTrack < 0) {
            if (targetDurationUs <= 0 || writtenPresentationTimeUs >= targetDurationUs) {
                isExtractorEOS = true;
                decoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                return DRAIN_STATE_NONE;
            }
            loopOffsetUs += Math.max(estimateLoopDurationUs(), 0);
            extractor.seekTo(0, SEEK_TO_PREVIOUS_SYNC);
            return DRAIN_STATE_NONE;
        }

        int sampleSize = extractor.readSampleData(decoderBuffers.getInputBuffer(result), 0);
        long sampleTime = extractor.getSampleTime();
        long ptsUs = loopOffsetUs + sampleTime;

        if (targetDurationUs > 0 && ptsUs >= targetDurationUs) {
            isExtractorEOS = true;
            decoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }

        boolean isKeyFrame = (extractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        decoder.queueInputBuffer(result, 0, sampleSize, ptsUs, isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0);
        prevSampleTimeUs = lastSampleTimeUs;
        lastSampleTimeUs = sampleTime;
        extractor.advance();
        return DRAIN_STATE_CONSUMED;
    }

    private int drainDecoder(long timeoutUs) {
        if (isDecoderEOS) return DRAIN_STATE_NONE;

        int result = decoder.dequeueOutputBuffer(bufferInfo, timeoutUs);
        switch (result) {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                audioChannel.setActualDecodedFormat(decoder.getOutputFormat());
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            isDecoderEOS = true;
            audioChannel.drainDecoderBufferAndQueue(AudioChannel.BUFFER_INDEX_END_OF_STREAM, 0);
        } else if (bufferInfo.size > 0) {
            audioChannel.drainDecoderBufferAndQueue(result, bufferInfo.presentationTimeUs);
        }

        return DRAIN_STATE_CONSUMED;
    }

    private int drainEncoder(long timeoutUs) {
        if (isEncoderEOS) return DRAIN_STATE_NONE;

        int result = encoder.dequeueOutputBuffer(bufferInfo, timeoutUs);
        switch (result) {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                if (actualOutputFormat != null) {
                    throw new ComposerException(ErrorCode.OUTPUT_FORMAT, "External audio output format changed twice.");
                }
                actualOutputFormat = encoder.getOutputFormat();
                muxer.setOutputFormat(SAMPLE_TYPE, actualOutputFormat);
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                encoderBuffers = new MediaCodecBufferCompatWrapper(encoder);
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        if (actualOutputFormat == null) {
            throw new ComposerException(ErrorCode.OUTPUT_FORMAT, "Could not determine actual output format.");
        }

        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            isEncoderEOS = true;
            bufferInfo.set(0, 0, 0, bufferInfo.flags);
        }
        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            encoder.releaseOutputBuffer(result, false);
            return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }
        if (targetDurationUs > 0 && bufferInfo.presentationTimeUs >= targetDurationUs) {
            isEncoderEOS = true;
            encoder.releaseOutputBuffer(result, false);
            return DRAIN_STATE_CONSUMED;
        }

        muxer.writeSampleData(SAMPLE_TYPE, encoderBuffers.getOutputBuffer(result), bufferInfo);
        writtenPresentationTimeUs = bufferInfo.presentationTimeUs;
        encoder.releaseOutputBuffer(result, false);
        return DRAIN_STATE_CONSUMED;
    }

    @Override
    public long getWrittenPresentationTimeUs() {
        return writtenPresentationTimeUs;
    }

    @Override
    public boolean isFinished() {
        return isEncoderEOS;
    }

    @Override
    public void release() {
        if (decoder != null) {
            if (decoderStarted) decoder.stop();
            decoder.release();
            decoder = null;
        }
        if (encoder != null) {
            if (encoderStarted) encoder.stop();
            encoder.release();
            encoder = null;
        }
        extractor.release();
    }

    private long estimateLoopDurationUs() {
        if (cachedDurationUs > 0) return cachedDurationUs;
        if (inputFormat != null && inputFormat.containsKey(MediaFormat.KEY_DURATION)) {
            cachedDurationUs = inputFormat.getLong(MediaFormat.KEY_DURATION);
            return cachedDurationUs;
        }
        if (lastSampleTimeUs > 0 && prevSampleTimeUs > 0) {
            cachedDurationUs = lastSampleTimeUs + (lastSampleTimeUs - prevSampleTimeUs);
        } else {
            cachedDurationUs = lastSampleTimeUs;
        }
        return cachedDurationUs;
    }

    private static int selectAudioTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("audio/")) {
                return i;
            }
        }
        return -1;
    }

    private static int getFormatInt(MediaFormat format, String key, int fallback) {
        if (format != null && format.containsKey(key)) {
            try {
                return format.getInteger(key);
            } catch (RuntimeException ignore) {
                // Fall through.
            }
        }
        return fallback;
    }
}
