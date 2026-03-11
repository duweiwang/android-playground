package com.example.lib_gles.video_filter.filter_impl;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.lib_gles.video_filter.core.filter.GlFilter;

/**
 * Vertical pulse scale effect:
 * - Scale Y from 1.0 to targetScaleY during shrinkDurationMs.
 * - Scale Y from targetScaleY back to 1.0 during expandDurationMs.
 * - Wait intervalMs, then repeat.
 */
public class GlPulseVerticalScaleFilter extends GlFilter {

    private static final String VERTEX_SHADER = ""
            + "attribute vec4 aPosition;\n"
            + "attribute vec4 aTextureCoord;\n"
            + "varying highp vec2 vTextureCoord;\n"
            + "uniform float uScaleY;\n"
            + "void main() {\n"
            + "    vec4 pos = aPosition;\n"
            + "    pos.y = pos.y * uScaleY;\n"
            + "    gl_Position = pos;\n"
            + "    vTextureCoord = aTextureCoord.xy;\n"
            + "}\n";

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying highp vec2 vTextureCoord;\n"
            + "uniform lowp sampler2D sTexture;\n"
            + "void main() {\n"
            + "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
            + "}\n";

    private int scaleYHandle = -1;

    private float targetScaleY = 0.7f;
    private float shrinkDurationMs = 200f;
    private float expandDurationMs = 200f;
    private float intervalMs = 1000f;
    private long firstPresentationMs = -1L;

    public GlPulseVerticalScaleFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public GlPulseVerticalScaleFilter(float targetScaleY) {
        this();
        this.targetScaleY = clamp(targetScaleY, 0.01f, 1.0f);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        scaleYHandle = GLES20.glGetUniformLocation(mProgramHandle, "uScaleY");
    }

    @Override
    public void onDraw(long presentationTimeUs) {
        // Keep one time base. presentationTimeUs is fed in nanoseconds in this pipeline.
        long nowMs = presentationTimeUs >= 0
                ? presentationTimeUs / 1_000_000L
                : SystemClock.uptimeMillis();
        if (firstPresentationMs < 0L || nowMs < firstPresentationMs) {
            firstPresentationMs = nowMs;
        }

        float elapsedMs = nowMs - firstPresentationMs;
        float effectMs = Math.max(1f, shrinkDurationMs + expandDurationMs);
        float cycleMs = Math.max(1f, effectMs + Math.max(0f, intervalMs));
        float phaseMs = elapsedMs % cycleMs;
        if (phaseMs >= effectMs) {
            GLES20.glUniform1f(scaleYHandle, 1.0f);
            return;
        }

        float scaleY;
        if (phaseMs < shrinkDurationMs) {
            float p = phaseMs / Math.max(1f, shrinkDurationMs); // 0..1
            scaleY = 1.0f + (targetScaleY - 1.0f) * p;
        } else {
            float p = (phaseMs - shrinkDurationMs) / Math.max(1f, expandDurationMs); // 0..1
            scaleY = targetScaleY + (1.0f - targetScaleY) * p;
        }
        GLES20.glUniform1f(scaleYHandle, scaleY);
    }

    public GlPulseVerticalScaleFilter setTargetScaleY(float targetScaleY) {
        this.targetScaleY = clamp(targetScaleY, 0.01f, 1.0f);
        return this;
    }

    public GlPulseVerticalScaleFilter setShrinkDurationMs(float shrinkDurationMs) {
        this.shrinkDurationMs = Math.max(1f, shrinkDurationMs);
        return this;
    }

    public GlPulseVerticalScaleFilter setExpandDurationMs(float expandDurationMs) {
        this.expandDurationMs = Math.max(1f, expandDurationMs);
        return this;
    }

    public GlPulseVerticalScaleFilter setIntervalMs(float intervalMs) {
        this.intervalMs = Math.max(0f, intervalMs);
        return this;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
