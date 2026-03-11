package com.example.lib_gles.video_filter.filter_impl;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;

import com.example.lib_gles.video_filter.core.filter.GlFilter;

import java.util.List;

/**
 * Radial spread color effect:
 * - Starts from center and spreads to edges repeatedly.
 * - Center stays the lightest area.
 * - Color cycles through a configurable color list.
 */
public class GlRadialSpreadColorFilter extends GlFilter {

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying highp vec2 textureCoordinate;\n"
            + "uniform lowp sampler2D sTexture;\n"
            + "uniform float uTime;\n"
            + "uniform float uCycleDuration;\n"
            + "uniform float uMaxIntensity;\n"
            + "uniform float uSpreadSoftness;\n"
            + "uniform int uColorCount;\n"
            + "uniform vec3 uColor0;\n"
            + "uniform vec3 uColor1;\n"
            + "uniform vec3 uColor2;\n"
            + "uniform vec3 uColor3;\n"
            + "\n"
            + "vec3 pickColor(int idx) {\n"
            + "    if (idx == 0) return uColor0;\n"
            + "    if (idx == 1) return uColor1;\n"
            + "    if (idx == 2) return uColor2;\n"
            + "    return uColor3;\n"
            + "}\n"
            + "\n"
            + "vec3 evalCycleColor(float phase, int count) {\n"
            + "    float c = float(count);\n"
            + "    float p = phase * c;\n"
            + "    int idx = int(floor(p));\n"
            + "    if (idx >= count) idx = count - 1;\n"
            + "    int nextIdx = idx + 1;\n"
            + "    if (nextIdx >= count) nextIdx = 0;\n"
            + "    float t = fract(p);\n"
            + "    vec3 a = pickColor(idx);\n"
            + "    vec3 b = pickColor(nextIdx);\n"
            + "    return mix(a, b, t);\n"
            + "}\n"
            + "\n"
            + "void main() {\n"
            + "    vec2 uv = textureCoordinate;\n"
            + "    vec4 base = texture2D(sTexture, uv);\n"
            + "\n"
            + "    vec2 p = uv - vec2(0.5, 0.5);\n"
            + "    float dist = length(p);\n"
            + "    float maxDist = 0.70710678;\n"
            + "\n"
            + "    float phase = fract(uTime / max(0.001, uCycleDuration));\n"
            + "    float radius = phase * maxDist;\n"
            + "\n"
            + "    float spread = 1.0 - smoothstep(radius, radius + uSpreadSoftness, dist);\n"
            + "    float centerCore = 1.0 - smoothstep(0.0, maxDist, dist);\n"
            + "    float centerBoost = centerCore * centerCore;\n"
            + "    float mask = clamp(max(spread, centerBoost), 0.0, 1.0);\n"
            + "\n"
            + "    int count = uColorCount;\n"
            + "    if (count < 1) count = 1;\n"
            + "    if (count > 4) count = 4;\n"
            + "    vec3 tint = evalCycleColor(phase, count);\n"
            + "\n"
            + "    float centerLight = 0.20 * centerBoost;\n"
            + "    vec3 tinted = mix(base.rgb, tint + vec3(centerLight), uMaxIntensity * mask);\n"
            + "    gl_FragColor = vec4(tinted, base.a);\n"
            + "}\n";

    private int timeHandle = -1;
    private int cycleDurationHandle = -1;
    private int maxIntensityHandle = -1;
    private int spreadSoftnessHandle = -1;
    private int colorCountHandle = -1;
    private int color0Handle = -1;
    private int color1Handle = -1;
    private int color2Handle = -1;
    private int color3Handle = -1;

    private float cycleDurationSec = 1.6f;
    private float maxIntensity = 0.55f;
    private float spreadSoftness = 0.10f;

    // Up to 4 RGB colors.
    private final float[][] colors = new float[][]{
            {1.00f, 0.52f, 0.52f},
            {1.00f, 0.88f, 0.45f},
            {0.45f, 0.90f, 1.00f},
            {0.78f, 0.50f, 1.00f}
    };
    private int colorCount = 4;

    public GlRadialSpreadColorFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        timeHandle = GLES20.glGetUniformLocation(mProgramHandle, "uTime");
        cycleDurationHandle = GLES20.glGetUniformLocation(mProgramHandle, "uCycleDuration");
        maxIntensityHandle = GLES20.glGetUniformLocation(mProgramHandle, "uMaxIntensity");
        spreadSoftnessHandle = GLES20.glGetUniformLocation(mProgramHandle, "uSpreadSoftness");
        colorCountHandle = GLES20.glGetUniformLocation(mProgramHandle, "uColorCount");
        color0Handle = GLES20.glGetUniformLocation(mProgramHandle, "uColor0");
        color1Handle = GLES20.glGetUniformLocation(mProgramHandle, "uColor1");
        color2Handle = GLES20.glGetUniformLocation(mProgramHandle, "uColor2");
        color3Handle = GLES20.glGetUniformLocation(mProgramHandle, "uColor3");
    }

    @Override
    public void onDraw(long presentationTimeUs) {
        float tSec = presentationTimeUs >= 0
                ? presentationTimeUs / 1_000_000_000f
                : (SystemClock.uptimeMillis() / 1000f);

        GLES20.glUniform1f(timeHandle, tSec);
        GLES20.glUniform1f(cycleDurationHandle, Math.max(0.1f, cycleDurationSec));
        GLES20.glUniform1f(maxIntensityHandle, clamp(maxIntensity, 0f, 1f));
        GLES20.glUniform1f(spreadSoftnessHandle, clamp(spreadSoftness, 0.005f, 0.4f));
        GLES20.glUniform1i(colorCountHandle, clampInt(colorCount, 1, 4));

        GLES20.glUniform3f(color0Handle, colors[0][0], colors[0][1], colors[0][2]);
        GLES20.glUniform3f(color1Handle, colors[1][0], colors[1][1], colors[1][2]);
        GLES20.glUniform3f(color2Handle, colors[2][0], colors[2][1], colors[2][2]);
        GLES20.glUniform3f(color3Handle, colors[3][0], colors[3][1], colors[3][2]);
    }

    public GlRadialSpreadColorFilter setCycleDurationSec(float cycleDurationSec) {
        this.cycleDurationSec = Math.max(0.1f, cycleDurationSec);
        return this;
    }

    public GlRadialSpreadColorFilter setMaxIntensity(float maxIntensity) {
        this.maxIntensity = clamp(maxIntensity, 0f, 1f);
        return this;
    }

    public GlRadialSpreadColorFilter setSpreadSoftness(float spreadSoftness) {
        this.spreadSoftness = clamp(spreadSoftness, 0.005f, 0.4f);
        return this;
    }

    /**
     * Set color list using rgb triplets in [0,1], supports 1~4 colors.
     * Example: setColorList(new float[][]{{1f,0f,0f},{0f,1f,0f},{0f,0f,1f}})
     */
    public GlRadialSpreadColorFilter setColorList(float[][] colorList) {
        if (colorList == null || colorList.length == 0) {
            return this;
        }
        int n = Math.min(4, colorList.length);
        for (int i = 0; i < n; i++) {
            float[] c = colorList[i];
            if (c == null || c.length < 3) {
                continue;
            }
            colors[i][0] = clamp(c[0], 0f, 1f);
            colors[i][1] = clamp(c[1], 0f, 1f);
            colors[i][2] = clamp(c[2], 0f, 1f);
        }
        colorCount = n;
        return this;
    }

    /**
     * Set color list using Android color ints (ARGB/RGB).
     * Alpha channel is ignored.
     */
    public GlRadialSpreadColorFilter setColorList(List<Integer> colorList) {
        if (colorList == null || colorList.isEmpty()) {
            return this;
        }
        int n = Math.min(4, colorList.size());
        for (int i = 0; i < n; i++) {
            int c = colorList.get(i);
            colors[i][0] = Color.red(c) / 255f;
            colors[i][1] = Color.green(c) / 255f;
            colors[i][2] = Color.blue(c) / 255f;
        }
        colorCount = n;
        return this;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private static int clampInt(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
