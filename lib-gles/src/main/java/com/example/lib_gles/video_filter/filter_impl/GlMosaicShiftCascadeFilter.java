package com.example.lib_gles.video_filter.filter_impl;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.lib_gles.video_filter.core.filter.GlFilter;
import com.example.lib_gles.video_filter.core.filter.GlFilterList;

import java.util.Map;

/**
 * Composite effect timeline:
 * 1) Keep mosaic = 40 for 1.0s.
 * 2) Instantly shift frame left by 30%, then return to original in 0.2s while mosaic 40 -> 20.
 * 3) Repeat once: shift/return in 0.2s while mosaic 20 -> 10.
 * 4) Repeat once: shift/return in 0.2s while mosaic 10 -> 0.
 * 5) After timeline ends, keep offset=0 and mosaic=0.
 */
public class GlMosaicShiftCascadeFilter extends GlFilter {

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying highp vec2 textureCoordinate;\n"
            + "uniform lowp sampler2D sTexture;\n"
            + "uniform vec2 uResolution;\n"
            + "uniform float uBlockSize;\n"
            + "uniform float uShiftX;\n"
            + "void main() {\n"
            + "    vec2 shiftedUv = vec2(clamp(textureCoordinate.x + uShiftX, 0.0, 1.0), textureCoordinate.y);\n"
            + "    if (uBlockSize <= 1.0) {\n"
            + "        gl_FragColor = texture2D(sTexture, shiftedUv);\n"
            + "        return;\n"
            + "    }\n"
            + "    vec2 pixel = shiftedUv * uResolution;\n"
            + "    vec2 block = floor(pixel / uBlockSize) * uBlockSize + vec2(uBlockSize * 0.5);\n"
            + "    vec2 uv = block / uResolution;\n"
            + "    gl_FragColor = texture2D(sTexture, uv);\n"
            + "}\n";

    private int resolutionHandle = -1;
    private int blockSizeHandle = -1;
    private int shiftXHandle = -1;

    private float holdMs = 1000f;
    private float stepMs = 200f;
    private float startMosaic = 40f;
    private float mid1Mosaic = 20f;
    private float mid2Mosaic = 10f;
    private float endMosaic = 0f;
    private float shiftX = 0.30f;

    private Float firstTimelineMs = null;

    public GlMosaicShiftCascadeFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        resolutionHandle = GLES20.glGetUniformLocation(mProgramHandle, "uResolution");
        blockSizeHandle = GLES20.glGetUniformLocation(mProgramHandle, "uBlockSize");
        shiftXHandle = GLES20.glGetUniformLocation(mProgramHandle, "uShiftX");
    }

    @Override
    protected void onDraw(long presentationTimeUs, Map<String, Integer> extraTextureIds) {
        if (mWidth <= 0 || mHeight <= 0) {
            return;
        }

        float timelineMs = readTimelineMs(extraTextureIds);
        if (firstTimelineMs == null) {
            firstTimelineMs = timelineMs;
        }
        float elapsed = Math.max(0f, timelineMs - firstTimelineMs);

        State state = computeState(elapsed);

        GLES20.glUniform2f(resolutionHandle, mWidth, mHeight);
        GLES20.glUniform1f(blockSizeHandle, Math.max(0f, state.blockSize));
        GLES20.glUniform1f(shiftXHandle, state.shift);
    }

    private float readTimelineMs(Map<String, Integer> extraTextureIds) {
//        if (extraTextureIds != null && extraTextureIds.containsKey(GlFilterList.EXTRA_PRESENTATION_TIME_MS)) {
//            return extraTextureIds.get(GlFilterList.EXTRA_PRESENTATION_TIME_MS);
//        }
        return SystemClock.uptimeMillis() % 600000L;
    }

    private State computeState(float elapsedMs) {
        State out = new State();
        float t0 = holdMs;
        float t1 = t0 + stepMs;
        float t2 = t1 + stepMs;
        float t3 = t2 + stepMs;

        if (elapsedMs < t0) {
            out.shift = 0f;
            out.blockSize = startMosaic;
            return out;
        }
        if (elapsedMs < t1) {
            float p = (elapsedMs - t0) / stepMs;
            out.shift = shiftX * (1f - p);
            out.blockSize = lerp(startMosaic, mid1Mosaic, p);
            return out;
        }
        if (elapsedMs < t2) {
            float p = (elapsedMs - t1) / stepMs;
            out.shift = shiftX * (1f - p);
            out.blockSize = lerp(mid1Mosaic, mid2Mosaic, p);
            return out;
        }
        if (elapsedMs < t3) {
            float p = (elapsedMs - t2) / stepMs;
            out.shift = shiftX * (1f - p);
            out.blockSize = lerp(mid2Mosaic, endMosaic, p);
            return out;
        }
        out.shift = 0f;
        out.blockSize = endMosaic;
        return out;
    }

    public GlMosaicShiftCascadeFilter setHoldMs(float holdMs) {
        this.holdMs = Math.max(1f, holdMs);
        return this;
    }

    public GlMosaicShiftCascadeFilter setStepMs(float stepMs) {
        this.stepMs = Math.max(1f, stepMs);
        return this;
    }

    public GlMosaicShiftCascadeFilter setShiftX(float shiftX) {
        this.shiftX = Math.max(0f, shiftX);
        return this;
    }

    public GlMosaicShiftCascadeFilter setMosaicLevels(float start, float level1, float level2, float end) {
        this.startMosaic = Math.max(0f, start);
        this.mid1Mosaic = Math.max(0f, level1);
        this.mid2Mosaic = Math.max(0f, level2);
        this.endMosaic = Math.max(0f, end);
        return this;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static class State {
        float shift;
        float blockSize;
    }
}
