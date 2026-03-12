
package com.example.lib_gles.video_filter.composer;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.example.lib_gles.video_filter.core.DecoderOutputSurface;
import com.example.lib_gles.video_filter.core.EncoderSurface;
import com.example.lib_gles.video_filter.core.bean.FillMode;
import com.example.lib_gles.video_filter.core.bean.FillModeCustomItem;
import com.example.lib_gles.video_filter.core.bean.Resolution;
import com.example.lib_gles.video_filter.core.bean.Rotation;
import com.example.lib_gles.video_filter.core.filter.GlFilter;
import com.example.lib_gles.video_filter.core.filter.GlFilterList;

import java.io.IOException;
import java.nio.ByteBuffer;

// Refer: https://android.googlesource.com/platform/cts/+/lollipop-release/tests/tests/media/src/android/media/cts/ExtractDecodeEditEncodeMuxTest.java
// Refer: https://github.com/ypresto/android-transcoder/blob/master/lib/src/main/java/net/ypresto/androidtranscoder/engine/VideoTrackTranscoder.java
class VideoComposer {
    private static final String TAG = "VComposer";
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final int DRAIN_STATE_CONSUMED = 2;

    private final MediaExtractor mediaExtractor;
    private final int trackIndex;
    private final MediaFormat outputFormat;
    private final MuxRender muxRender;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec decoder;
    private MediaCodec encoder;
    private ByteBuffer[] decoderInputBuffers;
    private ByteBuffer[] encoderOutputBuffers;
    private MediaFormat actualOutputFormat;
//    private DecoderSurface2 decoderSurface;
    private DecoderOutputSurface decoderSurface;
    private EncoderSurface encoderSurface;
    private boolean isExtractorEOS;
    private boolean isDecoderEOS;
    private boolean isEncoderEOS;
    private boolean decoderStarted;
    private boolean encoderStarted;
    private long writtenPresentationTimeUs;
    private final double timeScale;
    private GlFilter filter;
    private GlFilterList filterList;
    private long expectedOutputDurationUs = -1L;
    private long videoFrameCount = 0L;
    private long lastLoggedVideoPtsBucket = Long.MIN_VALUE;
    private long clipStartUs = -1L;
    private long clipEndUs = -1L;
    private long lastRenderedSourcePtsUs = Long.MIN_VALUE;
    private long lastRenderedOutputPtsUs = 0L;
    private long videoMuxPtsOffsetUs = Long.MIN_VALUE;

    VideoComposer(MediaExtractor mediaExtractor, int trackIndex,
                  MediaFormat outputFormat, MuxRender muxRender, double timeScale) {
        this.mediaExtractor = mediaExtractor;
        this.trackIndex = trackIndex;
        this.outputFormat = outputFormat;
        this.muxRender = muxRender;
        this.timeScale = timeScale;
    }


    void setUp(GlFilter filter,
               GlFilterList filterList,
               Rotation rotation,
               Resolution outputResolution,
               Resolution inputResolution,
               FillMode fillMode,
               FillModeCustomItem fillModeCustomItem,
               final boolean flipVertical,
               final boolean flipHorizontal) {
        mediaExtractor.selectTrack(trackIndex);
        try {
            encoder = MediaCodec.createEncoderByType(outputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new ComposerException(ErrorCode.CODEC_INIT, e);
        }
        encoder.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoderSurface = new EncoderSurface(encoder.createInputSurface());
        encoderSurface.makeCurrent();
        encoder.start();
        encoderStarted = true;
        encoderOutputBuffers = encoder.getOutputBuffers();

        MediaFormat inputFormat = mediaExtractor.getTrackFormat(trackIndex);
        if (inputFormat.containsKey("rotation-degrees")) {
            // Decoded video is rotated automatically in Android 5.0 lollipop.
            // Turn off here because we don't want to encode rotated one.
            // refer: https://android.googlesource.com/platform/frameworks/av/+blame/lollipop-release/media/libstagefright/Utils.cpp
            inputFormat.setInteger("rotation-degrees", 0);
        }

        decoderSurface = new DecoderOutputSurface(filter, filterList);
        this.filter = filter;
        this.filterList = filterList;
//        decoderSurface = new DecoderSurface2(new GlComposeFilter());
        decoderSurface.setRotation(rotation);
        decoderSurface.setOutputResolution(outputResolution);
        decoderSurface.setInputResolution(inputResolution);
        decoderSurface.setFillMode(fillMode);
        decoderSurface.setFillModeCustomItem(fillModeCustomItem);
        decoderSurface.setFlipHorizontal(flipHorizontal);
        decoderSurface.setFlipVertical(flipVertical);
        decoderSurface.setupAll();
        try {
            decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            throw new ComposerException(ErrorCode.CODEC_INIT, e);
        }
        decoder.configure(inputFormat, decoderSurface.getSurface(), null, 0);
        decoder.start();
        decoderStarted = true;
        decoderInputBuffers = decoder.getInputBuffers();
    }


    boolean stepPipeline() {
        boolean busy = false;

        int status;
        while (drainEncoder() != DRAIN_STATE_NONE) {
            busy = true;
        }
        do {
            status = drainDecoder();
            if (status != DRAIN_STATE_NONE) {
                busy = true;
            }
            // NOTE: not repeating to keep from deadlock when encoder is full.
        } while (status == DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY);
        while (drainExtractor() != DRAIN_STATE_NONE) {
            busy = true;
        }

        return busy;
    }


    long getWrittenPresentationTimeUs() {
        return writtenPresentationTimeUs;
    }


    boolean isFinished() {
        return isEncoderEOS;
    }


    void release() {
        if (decoderSurface != null) {
            decoderSurface.release();
            decoderSurface = null;
        }
        if (encoderSurface != null) {
            encoderSurface.release();
            encoderSurface = null;
        }
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
    }

    private int drainExtractor() {
        if (isExtractorEOS) return DRAIN_STATE_NONE;
        int trackIndex = mediaExtractor.getSampleTrackIndex();
        if (trackIndex >= 0 && trackIndex != this.trackIndex) {
            return DRAIN_STATE_NONE;
        }
        int result = decoder.dequeueInputBuffer(0);
        if (result < 0) return DRAIN_STATE_NONE;
        if (trackIndex < 0) {
            isExtractorEOS = true;
            decoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }
        int sampleSize = mediaExtractor.readSampleData(decoderInputBuffers[result], 0);
        boolean isKeyFrame = (mediaExtractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;

        long sampleTime = mediaExtractor.getSampleTime();
        if (sampleTime > clipEndUs && enableClip()) {
            isExtractorEOS = true;
            mediaExtractor.unselectTrack(this.trackIndex);
            decoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }

        decoder.queueInputBuffer(result, 0, sampleSize, sampleTime, isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0);
        mediaExtractor.advance();
        return DRAIN_STATE_CONSUMED;
    }

    /**
     * 尽量从解码器取解码帧并渲染
     * @return
     */
    private int drainDecoder() {
        if (isDecoderEOS) return DRAIN_STATE_NONE;
        int result = decoder.dequeueOutputBuffer(bufferInfo, 0);
        switch (result) {
            // 当前无可用输出，稍后重试。
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            // 输出状态变更，立即重试一次以尽快拿到有效帧。
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }
        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            Log.d(TAG+".drainDecoder", "drainDecoder: end of stream! bufferInfo.offset:"+bufferInfo.offset+", size:"+bufferInfo.size+",presentationTimeUs:"+bufferInfo.presentationTimeUs);
            encoder.signalEndOfInputStream();
            isDecoderEOS = true;
            bufferInfo.size = 0;
        }

        boolean codecConfigBuffer = (bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0;
        boolean preRollFrame = enableClip() && bufferInfo.presentationTimeUs < clipStartUs;
        boolean outOfClipEnd = enableClip() && clipEndUs > clipStartUs && bufferInfo.presentationTimeUs > clipEndUs;
        boolean doRender = bufferInfo.size > 0 && !codecConfigBuffer && !preRollFrame && !outOfClipEnd;



        // NOTE: doRender will block if buffer (of encoder) is full.
        // Refer: http://bigflake.com/mediacodec/CameraToMpegTest.java.txt
        // 归还 decoder 输出 buffer；doRender=true 时该 buffer 会被渲染到 SurfaceTexture。
        decoder.releaseOutputBuffer(result, doRender);
        if (doRender) {
            long renderPtsUs;
            long clipRelativeSourceUs = Math.max(0L, bufferInfo.presentationTimeUs - clipStartUs);
            if (lastRenderedSourcePtsUs == Long.MIN_VALUE) {
                renderPtsUs = 0L;
            } else {
                long deltaSourceUs = Math.max(0L, bufferInfo.presentationTimeUs - lastRenderedSourcePtsUs);
                double dynamicScale = sanitizeTimeScale(resolveTimeScaleAtMs(clipRelativeSourceUs / 1000L));
                long deltaOutUs = (long) (deltaSourceUs / dynamicScale);
                renderPtsUs = lastRenderedOutputPtsUs + Math.max(0L, deltaOutUs);
            }
            lastRenderedSourcePtsUs = bufferInfo.presentationTimeUs;
            lastRenderedOutputPtsUs = renderPtsUs;
            // 等待 GPU 拿到新图像，执行滤镜绘制，再把帧提交给 encoder 的输入 surface。
            long beforeRenderNs = System.nanoTime();
            decoderSurface.awaitNewImage();
            decoderSurface.drawImage(renderPtsUs * 1000);
            encoderSurface.setPresentationTime(renderPtsUs * 1000);
            encoderSurface.swapBuffers();
            videoFrameCount++;
            long renderCostMs = (System.nanoTime() - beforeRenderNs) / 1_000_000L;
        }
        return DRAIN_STATE_CONSUMED;
    }

    /**
     * 尽量从编码器取码流并写入 muxer
     * @return
     */
    private int drainEncoder() {
        if (isEncoderEOS) return DRAIN_STATE_NONE;
        int result = encoder.dequeueOutputBuffer(bufferInfo, 0);
        switch (result) {
            // 还没有编码产物，先返回。
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                // 第一次格式变更时拿到真实输出格式（含 csd），并通知 muxer 建轨。
                if (actualOutputFormat != null) {
                    throw new ComposerException(ErrorCode.OUTPUT_FORMAT, "Video output format changed twice.");
                }
                actualOutputFormat = encoder.getOutputFormat();
                muxRender.setOutputFormat(MuxRender.SampleType.VIDEO, actualOutputFormat);
                muxRender.onSetOutputFormat();
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                // 旧 API 需要刷新输出 buffer 数组引用。
                encoderOutputBuffers = encoder.getOutputBuffers();
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
            // SPS or PPS, which should be passed by MediaFormat.
            encoder.releaseOutputBuffer(result, false);
            return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }
        long muxPtsUs = bufferInfo.presentationTimeUs;
        if (bufferInfo.size > 0) {
            if (videoMuxPtsOffsetUs == Long.MIN_VALUE) {
                videoMuxPtsOffsetUs = muxPtsUs;
            }
            muxPtsUs = Math.max(0L, muxPtsUs - videoMuxPtsOffsetUs);
        }
        if (enableClip() && expectedOutputDurationUs > 0 && isExtractorEOS) {
            // bufferInfo.presentationTimeUs >= expectedOutputDurationUs 是为了控制输出文件的时长不超过clip的指定
            if (muxPtsUs >= expectedOutputDurationUs) {
                isEncoderEOS = true;
                encoder.releaseOutputBuffer(result, false);
                return DRAIN_STATE_CONSUMED;
            }
        }
        long originalPtsUs = bufferInfo.presentationTimeUs;
        bufferInfo.presentationTimeUs = muxPtsUs;
        muxRender.writeSampleData(MuxRender.SampleType.VIDEO, encoderOutputBuffers[result], bufferInfo);
        writtenPresentationTimeUs = muxPtsUs;
        bufferInfo.presentationTimeUs = originalPtsUs;
        encoder.releaseOutputBuffer(result, false);
        return DRAIN_STATE_CONSUMED;
    }

    private long startTimeMs, endTimeMs;

    private boolean enableClip() {
        return endTimeMs > startTimeMs && startTimeMs >= 0;
    }

    public void setClipRange(long startTimeMs, long endTimeMs) {
        this.startTimeMs = startTimeMs;
        this.endTimeMs = endTimeMs;
        clipStartUs = startTimeMs * 1000L;
        clipEndUs = endTimeMs * 1000L;
        mediaExtractor.seekTo(clipStartUs, SEEK_TO_PREVIOUS_SYNC);
        lastRenderedSourcePtsUs = Long.MIN_VALUE;
        lastRenderedOutputPtsUs = 0L;
        videoMuxPtsOffsetUs = Long.MIN_VALUE;
        expectedOutputDurationUs = computeExpectedOutputDurationUs();
    }


    private boolean hasDynamicTimeScale() {
        if (filterList != null && filterList.hasTimeScaleControl()) {
            return true;
        }
        return filter != null && filter.hasTimeScaleControl();
    }

    private double resolveTimeScaleAtMs(long presentationTimeMs) {
        if (filterList != null) {
            double scale = filterList.resolveTimeScaleAtMs(presentationTimeMs);
            if (Math.abs(scale - 1.0d) > 1e-9) {
                return scale;
            }
        }
        if (filter != null) {
            double scale = filter.resolveTimeScaleAtMs(presentationTimeMs);
            if (Math.abs(scale - 1.0d) > 1e-9) {
                return scale;
            }
        }
        return timeScale;
    }

    private static double sanitizeTimeScale(double scale) {
        if (Double.isNaN(scale) || Double.isInfinite(scale) || scale <= 0d) {
            return 1.0d;
        }
        return scale;
    }

    private long computeExpectedOutputDurationUs() {
        if (!enableClip()) {
            return -1L;
        }
        long clipDurationMs = Math.max(0L, endTimeMs - startTimeMs);
        if (clipDurationMs <= 0L) {
            return 0L;
        }
        if (!hasDynamicTimeScale()) {
            return (long) ((clipDurationMs * 1000d) / sanitizeTimeScale(timeScale));
        }
        double totalUs = 0d;
        // Integrate with 1ms step in clip-relative timeline.
        for (long tMs = 0L; tMs < clipDurationMs; tMs++) {
            double scale = sanitizeTimeScale(resolveTimeScaleAtMs(tMs));
            totalUs += 1000d / scale;
        }
        return (long) Math.ceil(totalUs);
    }
}
