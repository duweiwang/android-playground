package com.example.lib_gles.video_filter.composer;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class ExternalAudioComposer implements IAudioComposer {
    private final MediaExtractor extractor = new MediaExtractor();
    private final MuxRender muxRender;
    private final MuxRender.SampleType sampleType = MuxRender.SampleType.AUDIO;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    private final long targetDurationUs;

    private int trackIndex = -1;
    private int bufferSize;
    private ByteBuffer buffer;
    private boolean isEOS;
    private long writtenPresentationTimeUs;
    private long loopOffsetUs;
    private long lastSampleTimeUs;
    private long prevSampleTimeUs;
    private long cachedDurationUs = -1;

    ExternalAudioComposer(String audioPath, MuxRender muxRender, long targetDurationUs) throws IOException {
        this.muxRender = muxRender;
        this.targetDurationUs = targetDurationUs;
        extractor.setDataSource(audioPath);
        trackIndex = selectAudioTrack(extractor);
        if (trackIndex < 0) {
            throw new IllegalArgumentException("No audio track found in external audio.");
        }
        MediaFormat outputFormat = extractor.getTrackFormat(trackIndex);
        muxRender.setOutputFormat(sampleType, outputFormat);
        bufferSize = outputFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)
                ? outputFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
                : 1024 * 256;
        buffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
    }

    @Override
    public void setup() {
        extractor.selectTrack(trackIndex);
    }

    @Override
    public boolean stepPipeline() {
        if (isEOS) return false;

        int currentTrack = extractor.getSampleTrackIndex();
        if (currentTrack < 0) {
            if (targetDurationUs <= 0 || writtenPresentationTimeUs >= targetDurationUs) {
                buffer.clear();
                bufferInfo.set(0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                muxRender.writeSampleData(sampleType, buffer, bufferInfo);
                isEOS = true;
                return true;
            }
            loopOffsetUs += Math.max(estimateLoopDurationUs(), 0);
            extractor.seekTo(0, SEEK_TO_PREVIOUS_SYNC);
            return true;
        }
        if (currentTrack != trackIndex) return false;

        buffer.clear();
        int sampleSize = extractor.readSampleData(buffer, 0);
        if (sampleSize <= 0) {
            extractor.advance();
            return true;
        }
        boolean isKeyFrame = (extractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        int flags = isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0;
        long sampleTime = extractor.getSampleTime();
        long ptsUs = loopOffsetUs + sampleTime;

        if (targetDurationUs > 0 && ptsUs >= targetDurationUs) {
            buffer.clear();
            bufferInfo.set(0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            muxRender.writeSampleData(sampleType, buffer, bufferInfo);
            isEOS = true;
            return true;
        }

        bufferInfo.set(0, sampleSize, ptsUs, flags);
        muxRender.writeSampleData(sampleType, buffer, bufferInfo);
        writtenPresentationTimeUs = ptsUs;
        prevSampleTimeUs = lastSampleTimeUs;
        lastSampleTimeUs = sampleTime;
        extractor.advance();
        return true;
    }

    @Override
    public long getWrittenPresentationTimeUs() {
        return writtenPresentationTimeUs;
    }

    @Override
    public boolean isFinished() {
        return isEOS;
    }

    @Override
    public void release() {
        try {
            extractor.release();
        } catch (RuntimeException e) {
            // ignore
        }
    }

    private long estimateLoopDurationUs() {
        if (cachedDurationUs > 0) return cachedDurationUs;
        MediaFormat format = extractor.getTrackFormat(trackIndex);
        if (format.containsKey(MediaFormat.KEY_DURATION)) {
            cachedDurationUs = format.getLong(MediaFormat.KEY_DURATION);
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
}
