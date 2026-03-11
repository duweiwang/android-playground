package com.example.lib_gles.video_filter.filter_impl;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.lib_gles.video_filter.core.filter.GlFilter;

/**
 * Pulse zoom effect:
 * - Zoom in from 1.0 to targetScale during zoomInDurationMs.
 * - Zoom out from targetScale back to 1.0 during zoomOutDurationMs.
 * - Repeats forever.
 */
public class GlPulseZoomFilter extends GlFilter {

    private static final String VERTEX_SHADER = ""
            + "attribute vec4 aPosition;\n"
            + "attribute vec4 aTextureCoord;\n"
            + "varying highp vec2 vTextureCoord;\n"
            + "uniform float uScale;\n"
            + "void main() {\n"
            + "    gl_Position = aPosition;\n"
            + "    vec2 centered = aTextureCoord.xy - vec2(0.5, 0.5);\n"
            + "    vTextureCoord = centered / uScale + vec2(0.5, 0.5);\n"
            + "}\n";

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying highp vec2 vTextureCoord;\n"
            + "uniform lowp sampler2D sTexture;\n"
            + "void main() {\n"
            + "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
            + "}\n";

    private int scaleHandle = -1;

    private float targetScale = 1.3f;
    private float zoomInDurationMs = 1000f;
    private float zoomOutDurationMs = 1000f;

    public GlPulseZoomFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public GlPulseZoomFilter(float targetScale) {
        this();
        this.targetScale = Math.max(1.0f, targetScale);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        scaleHandle = GLES20.glGetUniformLocation(mProgramHandle, "uScale");
    }

    @Override
    public void onDraw(long presentationTimeUs) {
        float nowMs = SystemClock.uptimeMillis() % 600000L;
        float cycle = Math.max(1f, zoomInDurationMs + zoomOutDurationMs);
        float t = nowMs % cycle;

        float scale;
        if (t < zoomInDurationMs) {
            float p = t / Math.max(1f, zoomInDurationMs); // 0..1
            scale = 1.0f + (targetScale - 1.0f) * p;
        } else {
            float p = (t - zoomInDurationMs) / Math.max(1f, zoomOutDurationMs); // 0..1
            scale = targetScale + (1.0f - targetScale) * p;
        }
        GLES20.glUniform1f(scaleHandle, scale);
    }

    public GlPulseZoomFilter setTargetScale(float targetScale) {
        this.targetScale = Math.max(1.0f, targetScale);
        return this;
    }

    public GlPulseZoomFilter setZoomInDurationMs(float zoomInDurationMs) {
        this.zoomInDurationMs = Math.max(1f, zoomInDurationMs);
        return this;
    }

    public GlPulseZoomFilter setZoomOutDurationMs(float zoomOutDurationMs) {
        this.zoomOutDurationMs = Math.max(1f, zoomOutDurationMs);
        return this;
    }
}
