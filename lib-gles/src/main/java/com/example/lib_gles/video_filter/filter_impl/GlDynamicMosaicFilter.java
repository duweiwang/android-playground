package com.example.lib_gles.video_filter.filter_impl;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.lib_gles.video_filter.core.filter.GlFilter;
import com.example.lib_gles.video_filter.core.filter.GlFilterList;

import java.util.Map;

/**
 * Dynamic mosaic effect driven by timeline time.
 * Supports loop and one-shot modes.
 */
public class GlDynamicMosaicFilter extends GlFilter {

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying highp vec2 textureCoordinate;\n"
            + "uniform lowp sampler2D sTexture;\n"
            + "uniform vec2 uResolution;\n"
            + "uniform float uBlockSize;\n"
            + "void main() {\n"
            + "    vec2 pixel = textureCoordinate * uResolution;\n"
            + "    vec2 block = floor(pixel / uBlockSize) * uBlockSize + vec2(uBlockSize * 0.5);\n"
            + "    vec2 uv = block / uResolution;\n"
            + "    gl_FragColor = texture2D(sTexture, uv);\n"
            + "}\n";

    private int resolutionHandle = -1;
    private int blockSizeHandle = -1;

    private float minBlockSizePx = 2f;
    private float maxBlockSizePx = 36f;
    private float durationMs = 1000f;
    private boolean loop = true;
    private boolean pingPong = true; // 0->1->0 in one cycle

    public GlDynamicMosaicFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        resolutionHandle = GLES20.glGetUniformLocation(mProgramHandle, "uResolution");
        blockSizeHandle = GLES20.glGetUniformLocation(mProgramHandle, "uBlockSize");
    }

    @Override
    protected void onDraw(long presentationTimeUs, Map<String, Integer> extraTextureIds) {
        if (mWidth <= 0 || mHeight <= 0) {
            return;
        }

        float timelineMs = readTimelineMs(extraTextureIds);
        float progress = computeProgress(timelineMs);
        float block = minBlockSizePx + (maxBlockSizePx - minBlockSizePx) * progress;

        GLES20.glUniform2f(resolutionHandle, mWidth, mHeight);
        GLES20.glUniform1f(blockSizeHandle, Math.max(1f, block));
    }

    private float readTimelineMs(Map<String, Integer> extraTextureIds) {
//        if (extraTextureIds != null && extraTextureIds.containsKey(GlFilterList.EXTRA_PRESENTATION_TIME_MS)) {
//            return extraTextureIds.get(GlFilterList.EXTRA_PRESENTATION_TIME_MS);
//        }
        return SystemClock.uptimeMillis() % 600000L;
    }

    private float computeProgress(float timelineMs) {
        float d = Math.max(1f, durationMs);
        float p;
        if (loop) {
            p = (timelineMs % d) / d; // [0,1)
        } else {
            p = clamp(timelineMs / d, 0f, 1f);
        }
        if (pingPong) {
            p = 1f - Math.abs(2f * p - 1f);
        }
        return clamp(p, 0f, 1f);
    }

    public GlDynamicMosaicFilter setRange(float minBlockSizePx, float maxBlockSizePx) {
        this.minBlockSizePx = Math.max(1f, Math.min(minBlockSizePx, maxBlockSizePx));
        this.maxBlockSizePx = Math.max(this.minBlockSizePx, maxBlockSizePx);
        return this;
    }

    public GlDynamicMosaicFilter setDurationMs(float durationMs) {
        this.durationMs = Math.max(1f, durationMs);
        return this;
    }

    public GlDynamicMosaicFilter setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    public GlDynamicMosaicFilter setPingPong(boolean pingPong) {
        this.pingPong = pingPong;
        return this;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
