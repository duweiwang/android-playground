package com.example.lib_gles.video_filter.composer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

// Refer: https://github.com/ypresto/android-transcoder/blob/master/lib/src/main/java/net/ypresto/androidtranscoder/engine/QueuedMuxer.java

class MuxRender {
    private static final String TAG = "MuxRender";
    private static final int MAX_PENDING_BYTES = 32 * 1024 * 1024;
    private final MediaMuxer muxer;
    private final boolean audioTrackRequired;
    private MediaFormat videoFormat;
    private MediaFormat audioFormat;
    private int videoTrackIndex;
    private int audioTrackIndex;
    private final List<PendingSample> pendingSamples;
    private int pendingBytes;
    private boolean started;

    MuxRender(MediaMuxer muxer, boolean audioTrackRequired) {
        this.muxer = muxer;
        this.audioTrackRequired = audioTrackRequired;
        pendingSamples = new ArrayList<>();
    }

    void setOutputFormat(SampleType sampleType, MediaFormat format) {
        switch (sampleType) {
            case VIDEO:
                videoFormat = format;
                break;
            case AUDIO:
                audioFormat = format;
                break;
            default:
                throw new AssertionError();
        }
        maybeStartMuxer();
    }

    void onSetOutputFormat() {
        maybeStartMuxer();
    }

    void writeSampleData(SampleType sampleType, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) {
        if (started) {
            muxer.writeSampleData(getTrackIndexForSampleType(sampleType), byteBuf, bufferInfo);
            return;
        }
        if (bufferInfo.size <= 0) {
            return;
        }
        int nextPendingBytes = pendingBytes + bufferInfo.size;
        if (nextPendingBytes > MAX_PENDING_BYTES) {
            String msg = "MuxRender pending sample bytes exceed limit before muxer start. "
                    + "sampleType=" + sampleType
                    + ", sampleSize=" + bufferInfo.size
                    + ", samplePtsUs=" + bufferInfo.presentationTimeUs
                    + ", pendingSamples=" + pendingSamples.size()
                    + ", pendingBytes=" + pendingBytes
                    + ", limit=" + MAX_PENDING_BYTES;
            throw new ComposerException(ErrorCode.MUXER, msg);
        }
        ByteBuffer duplicated = byteBuf.duplicate();
        duplicated.position(bufferInfo.offset);
        duplicated.limit(bufferInfo.offset + bufferInfo.size);
        ByteBuffer sampleData = ByteBuffer.allocateDirect(bufferInfo.size);
        sampleData.put(duplicated);
        sampleData.flip();

        MediaCodec.BufferInfo copiedBufferInfo = new MediaCodec.BufferInfo();
        copiedBufferInfo.set(0, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
        pendingSamples.add(new PendingSample(sampleType, sampleData, copiedBufferInfo));
        pendingBytes = nextPendingBytes;
    }

    private int getTrackIndexForSampleType(SampleType sampleType) {
        switch (sampleType) {
            case VIDEO:
                return videoTrackIndex;
            case AUDIO:
                return audioTrackIndex;
            default:
                throw new AssertionError();
        }
    }

    private void maybeStartMuxer() {
        if (started || videoFormat == null) {
            return;
        }
        if (audioTrackRequired && audioFormat == null) {
            return;
        }

        videoTrackIndex = muxer.addTrack(videoFormat);
        Log.v(TAG, "Added track #" + videoTrackIndex + " with " + videoFormat.getString(MediaFormat.KEY_MIME) + " to muxer");

        if (audioTrackRequired) {
            audioTrackIndex = muxer.addTrack(audioFormat);
            Log.v(TAG, "Added track #" + audioTrackIndex + " with " + audioFormat.getString(MediaFormat.KEY_MIME) + " to muxer");
        }

        muxer.start();
        started = true;

        Log.v(TAG, "Output format determined, writing " + pendingSamples.size()
                + " pending samples / " + pendingBytes + " bytes to muxer.");
        for (PendingSample sample : pendingSamples) {
            muxer.writeSampleData(getTrackIndexForSampleType(sample.sampleType), sample.data, sample.bufferInfo);
        }
        pendingSamples.clear();
        pendingBytes = 0;
    }

    public enum SampleType {VIDEO, AUDIO}

    private static class PendingSample {
        private final SampleType sampleType;
        private final ByteBuffer data;
        private final MediaCodec.BufferInfo bufferInfo;

        private PendingSample(SampleType sampleType, ByteBuffer data, MediaCodec.BufferInfo bufferInfo) {
            this.sampleType = sampleType;
            this.data = data;
            this.bufferInfo = bufferInfo;
        }
    }

}
