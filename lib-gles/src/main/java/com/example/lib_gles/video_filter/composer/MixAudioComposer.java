package com.example.lib_gles.video_filter.composer;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

class MixAudioComposer implements IAudioComposer {
    private static final MuxRender.SampleType SAMPLE_TYPE = MuxRender.SampleType.AUDIO;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final int DRAIN_STATE_CONSUMED = 2;

    private final MediaExtractor mainExtractor;
    private final int mainTrackIndex;
    private final MediaExtractor extExtractor = new MediaExtractor();
    private final int extTrackIndex;
    private MediaFormat encoderFormat;
    private final MuxRender muxer;
    private final long targetDurationUs;
    private final long startTimeMs;
    private final long endTimeMs;

    private final MediaCodec.BufferInfo encoderBufferInfo = new MediaCodec.BufferInfo();
    private final MediaCodec.BufferInfo mainDecoderBufferInfo = new MediaCodec.BufferInfo();
    private final MediaCodec.BufferInfo extDecoderBufferInfo = new MediaCodec.BufferInfo();

    private MediaCodec mainDecoder;
    private MediaCodec extDecoder;
    private MediaCodec encoder;
    private MediaCodecBufferCompatWrapper mainDecoderBuffers;
    private MediaCodecBufferCompatWrapper extDecoderBuffers;
    private MediaCodecBufferCompatWrapper encoderBuffers;

    private boolean mainExtractorEOS;
    private boolean extExtractorEOS;
    private boolean mainDecoderEOS;
    private boolean extDecoderEOS;
    private boolean encoderEOS;
    private boolean mainDecoderStarted;
    private boolean extDecoderStarted;
    private boolean encoderStarted;

    private int sampleRate;
    private int channelCount;
    private int extSampleRate;
    private int extChannelCount;
    private long writtenPresentationTimeUs;
    private long extLoopOffsetUs;
    private long extLastSampleTimeUs;
    private long extPrevSampleTimeUs;
    private long cachedExtDurationUs = -1;

    private final Queue<PcmBuffer> mainQueue = new ArrayDeque<>();
    private final Queue<PcmBuffer> extQueue = new ArrayDeque<>();
    private PcmBuffer currentMain;
    private PcmBuffer currentExt;
    private boolean mainTrackUnselected;

    private final float mainVolume = 0.5f;
    private final float extVolume = 0.5f;

    MixAudioComposer(MediaExtractor mainExtractor,
                     int mainTrackIndex,
                     String externalAudioPath,
                     MuxRender muxer,
                     long targetDurationUs,
                     long startTimeMs,
                     long endTimeMs) throws IOException {
        this.mainExtractor = mainExtractor;
        this.mainTrackIndex = mainTrackIndex;
        this.muxer = muxer;
        this.targetDurationUs = targetDurationUs;
        this.startTimeMs = startTimeMs;
        this.endTimeMs = endTimeMs;

        extExtractor.setDataSource(externalAudioPath);
        this.extTrackIndex = selectAudioTrack(extExtractor);
        if (this.extTrackIndex < 0) {
            throw new IllegalArgumentException("No audio track found in external audio.");
        }
        // encoder format will be built in setup based on main audio format
    }

    @Override
    public void setup() {
        mainExtractor.selectTrack(mainTrackIndex);
        extExtractor.selectTrack(extTrackIndex);

        final MediaFormat mainInputFormat = mainExtractor.getTrackFormat(mainTrackIndex);
        sampleRate = mainInputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        channelCount = mainInputFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);

        encoderFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", sampleRate, channelCount);
        encoderFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        encoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128_000);
        encoderFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16 * 1024);

        try {
            encoder = MediaCodec.createEncoderByType(encoderFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        encoder.configure(encoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoder.start();
        encoderStarted = true;
        encoderBuffers = new MediaCodecBufferCompatWrapper(encoder);

        if (mainInputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE) != sampleRate ||
                mainInputFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT) != channelCount) {
            throw new UnsupportedOperationException("Main audio format does not match output format.");
        }
        try {
            mainDecoder = MediaCodec.createDecoderByType(mainInputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        mainDecoder.configure(mainInputFormat, null, null, 0);
        mainDecoder.start();
        mainDecoderStarted = true;
        mainDecoderBuffers = new MediaCodecBufferCompatWrapper(mainDecoder);

        final MediaFormat extInputFormat = extExtractor.getTrackFormat(extTrackIndex);
        extSampleRate = extInputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        extChannelCount = extInputFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        try {
            extDecoder = MediaCodec.createDecoderByType(extInputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        extDecoder.configure(extInputFormat, null, null, 0);
        extDecoder.start();
        extDecoderStarted = true;
        extDecoderBuffers = new MediaCodecBufferCompatWrapper(extDecoder);
    }

    @Override
    public boolean stepPipeline() {
        boolean busy = false;

        int status;
        while (drainEncoder(0) != DRAIN_STATE_NONE) busy = true;

        do {
            status = drainMainDecoder(0);
            if (status != DRAIN_STATE_NONE) busy = true;
        } while (status == DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY);

        do {
            status = drainExtDecoder(0);
            if (status != DRAIN_STATE_NONE) busy = true;
        } while (status == DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY);

        while (feedEncoder(0)) busy = true;

        while (drainMainExtractor(0) != DRAIN_STATE_NONE) busy = true;
        while (drainExtExtractor(0) != DRAIN_STATE_NONE) busy = true;

        return busy;
    }

    @Override
    public long getWrittenPresentationTimeUs() {
        return writtenPresentationTimeUs;
    }

    @Override
    public boolean isFinished() {
        return encoderEOS;
    }

    @Override
    public void release() {
        try {
            if (mainDecoder != null) {
                if (mainDecoderStarted) mainDecoder.stop();
                mainDecoder.release();
                mainDecoder = null;
            }
            if (extDecoder != null) {
                if (extDecoderStarted) extDecoder.stop();
                extDecoder.release();
                extDecoder = null;
            }
            if (encoder != null) {
                if (encoderStarted) encoder.stop();
                encoder.release();
                encoder = null;
            }
            extExtractor.release();
        } catch (RuntimeException e) {
            // ignore
        }
    }

    private int drainMainExtractor(long timeoutUs) {
        if (mainExtractorEOS) return DRAIN_STATE_NONE;
        int trackIndex = mainExtractor.getSampleTrackIndex();
        if (trackIndex >= 0 && trackIndex != mainTrackIndex) {
            return DRAIN_STATE_NONE;
        }
        final int result = mainDecoder.dequeueInputBuffer(timeoutUs);
        if (result < 0) return DRAIN_STATE_NONE;
        if (trackIndex < 0) {
            mainExtractorEOS = true;
            mainDecoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }

        ByteBuffer inputBuffer = mainDecoderBuffers.getInputBuffer(result);
        int sampleSize = mainExtractor.readSampleData(inputBuffer, 0);
        long sampleTime = mainExtractor.getSampleTime();
        if (sampleTime > endTimeMs * 1000 && enableClip()) {
            mainExtractor.unselectTrack(mainTrackIndex);
            mainExtractorEOS = true;
            mainDecoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }
        boolean isKeyFrame = (mainExtractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        mainDecoder.queueInputBuffer(result, 0, sampleSize, sampleTime, isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0);
        mainExtractor.advance();
        return DRAIN_STATE_CONSUMED;
    }

    private int drainExtExtractor(long timeoutUs) {
        if (extExtractorEOS) return DRAIN_STATE_NONE;
        int trackIndex = extExtractor.getSampleTrackIndex();
        if (trackIndex >= 0 && trackIndex != extTrackIndex) {
            return DRAIN_STATE_NONE;
        }
        final int result = extDecoder.dequeueInputBuffer(timeoutUs);
        if (result < 0) return DRAIN_STATE_NONE;
        if (trackIndex < 0) {
            if (targetDurationUs <= 0 || writtenPresentationTimeUs >= targetDurationUs) {
                extExtractorEOS = true;
                extDecoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                return DRAIN_STATE_NONE;
            }
            extLoopOffsetUs += Math.max(estimateExtLoopDurationUs(), 0);
            extExtractor.seekTo(0, SEEK_TO_PREVIOUS_SYNC);
            return DRAIN_STATE_NONE;
        }

        ByteBuffer inputBuffer = extDecoderBuffers.getInputBuffer(result);
        int sampleSize = extExtractor.readSampleData(inputBuffer, 0);
        long sampleTime = extExtractor.getSampleTime();
        long ptsUs = extLoopOffsetUs + sampleTime;
        if (targetDurationUs > 0 && ptsUs >= targetDurationUs) {
            extExtractorEOS = true;
            extDecoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }
        boolean isKeyFrame = (extExtractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        extDecoder.queueInputBuffer(result, 0, sampleSize, ptsUs, isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0);
        extPrevSampleTimeUs = extLastSampleTimeUs;
        extLastSampleTimeUs = sampleTime;
        extExtractor.advance();
        return DRAIN_STATE_CONSUMED;
    }

    private int drainMainDecoder(long timeoutUs) {
        if (mainDecoderEOS) return DRAIN_STATE_NONE;
        int result = mainDecoder.dequeueOutputBuffer(mainDecoderBufferInfo, timeoutUs);
        switch (result) {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }
        if ((mainDecoderBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            mainDecoderEOS = true;
            enqueuePcmBuffer(mainQueue, mainDecoderBuffers, result, mainDecoderBufferInfo, true, false);
            return DRAIN_STATE_CONSUMED;
        } else if (mainDecoderBufferInfo.size > 0) {
            enqueuePcmBuffer(mainQueue, mainDecoderBuffers, result, mainDecoderBufferInfo, false, false);
        }
        return DRAIN_STATE_CONSUMED;
    }

    private int drainExtDecoder(long timeoutUs) {
        if (extDecoderEOS) return DRAIN_STATE_NONE;
        int result = extDecoder.dequeueOutputBuffer(extDecoderBufferInfo, timeoutUs);
        switch (result) {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }
        if ((extDecoderBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            extDecoderEOS = true;
            enqueuePcmBuffer(extQueue, extDecoderBuffers, result, extDecoderBufferInfo, true, true);
            return DRAIN_STATE_CONSUMED;
        } else if (extDecoderBufferInfo.size > 0) {
            enqueuePcmBuffer(extQueue, extDecoderBuffers, result, extDecoderBufferInfo, false, true);
        }
        return DRAIN_STATE_CONSUMED;
    }

    private void enqueuePcmBuffer(Queue<PcmBuffer> queue,
                                  MediaCodecBufferCompatWrapper buffers,
                                  int bufferIndex,
                                  MediaCodec.BufferInfo info,
                                  boolean endOfStream,
                                  boolean isExternal) {
        ByteBuffer data = null;
        ShortBuffer shortBuffer = null;
        if (!endOfStream) {
            data = buffers.getOutputBuffer(bufferIndex);
            if (data == null) {
                return;
            }
            data.position(info.offset);
            data.limit(info.offset + info.size);
            shortBuffer = data.slice().order(ByteOrder.nativeOrder()).asShortBuffer();
            if (isExternal && (extSampleRate != sampleRate || extChannelCount != channelCount)) {
                shortBuffer = convertPcm(shortBuffer, extSampleRate, extChannelCount, sampleRate, channelCount);
            }
        }
        PcmBuffer pcmBuffer = new PcmBuffer();
        pcmBuffer.bufferIndex = bufferIndex;
        pcmBuffer.presentationTimeUs = info.presentationTimeUs;
        pcmBuffer.data = shortBuffer;
        pcmBuffer.endOfStream = endOfStream;
        queue.add(pcmBuffer);
    }

    private static ShortBuffer convertPcm(ShortBuffer src,
                                          int srcSampleRate,
                                          int srcChannels,
                                          int dstSampleRate,
                                          int dstChannels) {
        int srcSamples = src.remaining();
        if (srcSamples == 0) {
            return ShortBuffer.allocate(0);
        }
        int srcFrames = srcSamples / srcChannels;
        int dstFrames = (int) Math.max(1, (long) srcFrames * dstSampleRate / srcSampleRate);
        int dstSamples = dstFrames * dstChannels;
        ShortBuffer dst = ByteBuffer.allocateDirect(dstSamples * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();

        for (int i = 0; i < dstFrames; i++) {
            float srcFramePos = (float) i * srcSampleRate / dstSampleRate;
            int srcIndex = (int) srcFramePos;
            if (srcIndex >= srcFrames) {
                srcIndex = srcFrames - 1;
            }
            int base = src.position() + srcIndex * srcChannels;
            short left;
            short right;
            if (srcChannels == 1) {
                left = src.get(base);
                right = left;
            } else {
                left = src.get(base);
                right = src.get(base + 1);
            }

            if (dstChannels == 1) {
                int mixed = (left + right) / 2;
                dst.put((short) mixed);
            } else {
                dst.put(left);
                dst.put(right);
            }
        }
        dst.flip();
        return dst;
    }

    private boolean feedEncoder(long timeoutUs) {
        if (encoderEOS) return false;
        int inputIndex = encoder.dequeueInputBuffer(timeoutUs);
        if (inputIndex < 0) return false;

        if (currentMain == null) currentMain = mainQueue.poll();
        if (currentExt == null) currentExt = extQueue.poll();

        long startPtsUs = Long.MAX_VALUE;
        if (currentMain != null && !currentMain.endOfStream) {
            startPtsUs = Math.min(startPtsUs, currentMain.currentPtsUs(sampleRate, channelCount));
        }
        if (currentExt != null && !currentExt.endOfStream) {
            startPtsUs = Math.min(startPtsUs, currentExt.currentPtsUs(sampleRate, channelCount));
        }

        if (startPtsUs == Long.MAX_VALUE) {
            if (mainDecoderEOS && extDecoderEOS && mainQueue.isEmpty() && extQueue.isEmpty()) {
                encoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                return true;
            }
            return false;
        }

        if (targetDurationUs > 0 && startPtsUs >= targetDurationUs) {
            encoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return true;
        }

        ShortBuffer outBuffer = encoderBuffers.getInputBuffer(inputIndex).asShortBuffer();
        outBuffer.clear();

        while (outBuffer.hasRemaining()) {
            if (currentMain != null && !currentMain.hasRemaining()) {
                releaseMainBuffer();
                if (currentMain == null) currentMain = mainQueue.poll();
            }
            if (currentExt != null && !currentExt.hasRemaining()) {
                releaseExtBuffer();
                if (currentExt == null) currentExt = extQueue.poll();
            }

            if (currentMain == null && currentExt == null) break;
            if (currentMain != null && currentMain.endOfStream) {
                releaseMainBuffer();
                if (currentMain == null) currentMain = mainQueue.poll();
                continue;
            }
            if (currentExt != null && currentExt.endOfStream) {
                releaseExtBuffer();
                if (currentExt == null) currentExt = extQueue.poll();
                continue;
            }

            short mainSample = 0;
            short extSample = 0;
            if (currentMain != null) {
                mainSample = currentMain.data.get();
            }
            if (currentExt != null) {
                extSample = currentExt.data.get();
            }
            int mixed = (int) (mainSample * mainVolume + extSample * extVolume);
            if (mixed > Short.MAX_VALUE) mixed = Short.MAX_VALUE;
            if (mixed < Short.MIN_VALUE) mixed = Short.MIN_VALUE;
            outBuffer.put((short) mixed);
        }

        int size = outBuffer.position() * 2;
        if (size == 0) {
            return false;
        }
        encoder.queueInputBuffer(inputIndex, 0, size, startPtsUs, 0);
        return true;
    }

    private int drainEncoder(long timeoutUs) {
        if (encoderEOS) return DRAIN_STATE_NONE;
        int result = encoder.dequeueOutputBuffer(encoderBufferInfo, timeoutUs);
        switch (result) {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                muxer.setOutputFormat(SAMPLE_TYPE, encoder.getOutputFormat());
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                encoderBuffers = new MediaCodecBufferCompatWrapper(encoder);
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        if ((encoderBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            encoderEOS = true;
            unselectMainTrackIfNeeded();
            encoderBufferInfo.set(0, 0, 0, encoderBufferInfo.flags);
        }
        if ((encoderBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            encoder.releaseOutputBuffer(result, false);
            return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        muxer.writeSampleData(SAMPLE_TYPE, encoderBuffers.getOutputBuffer(result), encoderBufferInfo);
        writtenPresentationTimeUs = encoderBufferInfo.presentationTimeUs;
        encoder.releaseOutputBuffer(result, false);
        return DRAIN_STATE_CONSUMED;
    }

    private void releaseMainBuffer() {
        if (currentMain != null) {
            mainDecoder.releaseOutputBuffer(currentMain.bufferIndex, false);
            currentMain = null;
        }
    }

    private void releaseExtBuffer() {
        if (currentExt != null) {
            extDecoder.releaseOutputBuffer(currentExt.bufferIndex, false);
            currentExt = null;
        }
    }

    private void unselectMainTrackIfNeeded() {
        if (!mainTrackUnselected) {
            try {
                mainExtractor.unselectTrack(mainTrackIndex);
            } catch (IllegalStateException e) {
                // ignore
            }
            mainTrackUnselected = true;
        }
    }

    private boolean enableClip() {
        return endTimeMs > startTimeMs && startTimeMs >= 0;
    }

    private long estimateExtLoopDurationUs() {
        if (cachedExtDurationUs > 0) return cachedExtDurationUs;
        MediaFormat format = extExtractor.getTrackFormat(extTrackIndex);
        if (format.containsKey(MediaFormat.KEY_DURATION)) {
            cachedExtDurationUs = format.getLong(MediaFormat.KEY_DURATION);
            return cachedExtDurationUs;
        }
        if (extLastSampleTimeUs > 0 && extPrevSampleTimeUs > 0) {
            cachedExtDurationUs = extLastSampleTimeUs + (extLastSampleTimeUs - extPrevSampleTimeUs);
        } else {
            cachedExtDurationUs = extLastSampleTimeUs;
        }
        return cachedExtDurationUs;
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

    private static class PcmBuffer {
        int bufferIndex;
        long presentationTimeUs;
        ShortBuffer data;
        boolean endOfStream;

        boolean hasRemaining() {
            return data != null && data.hasRemaining();
        }

        long currentPtsUs(int sampleRate, int channelCount) {
            if (data == null) return presentationTimeUs;
            int consumedSamples = data.position();
            long consumedUs = (long) consumedSamples * 1_000_000L / sampleRate / channelCount;
            return presentationTimeUs + consumedUs;
        }
    }
}
