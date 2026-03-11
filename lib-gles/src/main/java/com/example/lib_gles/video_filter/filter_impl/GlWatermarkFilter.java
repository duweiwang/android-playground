package com.example.lib_gles.video_filter.filter_impl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.lib_gles.video_filter.core.filter.GlFilter;

public class GlWatermarkFilter extends GlFilter {

    private static final String FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying vec2 textureCoordinate;\n"
            + "uniform sampler2D sTexture;\n"
            + "uniform sampler2D uWatermarkTexture;\n"
            + "uniform vec2 uWmOrigin;\n"
            + "uniform vec2 uWmSize;\n"
            + "uniform float uWmAlpha;\n"
            + "void main() {\n"
            + "    vec4 baseColor = texture2D(sTexture, textureCoordinate);\n"
            + "    vec2 localCoord = (textureCoordinate - uWmOrigin) / uWmSize;\n"
            + "    float inX = step(0.0, localCoord.x) * step(localCoord.x, 1.0);\n"
            + "    float inY = step(0.0, localCoord.y) * step(localCoord.y, 1.0);\n"
            + "    float inRect = inX * inY;\n"
            + "    vec2 wmCoord = vec2(localCoord.x, 1.0 - localCoord.y);\n"
            + "    vec4 markColor = texture2D(uWatermarkTexture, wmCoord);\n"
            + "    float alpha = markColor.a * uWmAlpha * inRect;\n"
            + "    gl_FragColor = mix(baseColor, vec4(markColor.rgb, 1.0), alpha);\n"
            + "}\n";

    private final Bitmap watermarkBitmap;
    private final float marginPx;
    private final float scale;
    private final float alpha;
    private Position position = Position.RIGHT_BOTTOM;

    private int watermarkTextureId = 0;
    private int watermarkSamplerHandle = -1;
    private int wmOriginHandle = -1;
    private int wmSizeHandle = -1;
    private int wmAlphaHandle = -1;

    public GlWatermarkFilter(Bitmap watermarkBitmap, float marginPx, float scale, float alpha) {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
        this.watermarkBitmap = watermarkBitmap;
        this.marginPx = marginPx;
        this.scale = scale;
        this.alpha = alpha;
    }

    public GlWatermarkFilter(Bitmap watermarkBitmap, float marginPx, float scale, float alpha, Position position) {
        this(watermarkBitmap, marginPx, scale, alpha);
        this.position = position;
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        watermarkSamplerHandle = GLES20.glGetUniformLocation(mProgramHandle, "uWatermarkTexture");
        wmOriginHandle = GLES20.glGetUniformLocation(mProgramHandle, "uWmOrigin");
        wmSizeHandle = GLES20.glGetUniformLocation(mProgramHandle, "uWmSize");
        wmAlphaHandle = GLES20.glGetUniformLocation(mProgramHandle, "uWmAlpha");
        uploadWatermarkTextureIfNeed();
    }

    @Override
    public void onDraw(long presentationTimeUs) {
        if (mWidth <= 0 || mHeight <= 0 || watermarkBitmap == null || watermarkBitmap.isRecycled()) {
            return;
        }
        uploadWatermarkTextureIfNeed();
        if (watermarkTextureId == 0) {
            return;
        }

        float wmWidth = watermarkBitmap.getWidth() * scale;
        float wmHeight = watermarkBitmap.getHeight() * scale;
        float originX = getOriginX(wmWidth);
        float originY = getOriginY(wmHeight);
        float sizeX = wmWidth / mWidth;
        float sizeY = wmHeight / mHeight;

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, watermarkTextureId);
        GLES20.glUniform1i(watermarkSamplerHandle, 1);
        GLES20.glUniform2f(wmOriginHandle, originX, originY);
        GLES20.glUniform2f(wmSizeHandle, sizeX, sizeY);
        GLES20.glUniform1f(wmAlphaHandle, alpha);
    }

    @Override
    public void release() {
        if (watermarkTextureId != 0) {
            GLES20.glDeleteTextures(1, new int[]{watermarkTextureId}, 0);
            watermarkTextureId = 0;
        }
        super.release();
    }

    private void uploadWatermarkTextureIfNeed() {
        if (watermarkTextureId != 0 || watermarkBitmap == null || watermarkBitmap.isRecycled()) {
            return;
        }
        int[] ids = new int[1];
        GLES20.glGenTextures(1, ids, 0);
        watermarkTextureId = ids[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, watermarkTextureId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, watermarkBitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private float getOriginX(float wmWidth) {
        switch (position) {
            case LEFT_TOP:
            case LEFT_CENTER:
            case LEFT_BOTTOM:
                return marginPx / mWidth;
            case CENTER_TOP:
            case CENTER:
            case CENTER_BOTTOM:
                return (mWidth - wmWidth) * 0.5f / mWidth;
            case RIGHT_TOP:
            case RIGHT_CENTER:
            case RIGHT_BOTTOM:
            default:
                return (mWidth - wmWidth - marginPx) / mWidth;
        }
    }

    private float getOriginY(float wmHeight) {
        switch (position) {
            case LEFT_TOP:
            case CENTER_TOP:
            case RIGHT_TOP:
                return (mHeight - wmHeight - marginPx) / mHeight;
            case LEFT_CENTER:
            case CENTER:
            case RIGHT_CENTER:
                return (mHeight - wmHeight) * 0.5f / mHeight;
            case LEFT_BOTTOM:
            case CENTER_BOTTOM:
            case RIGHT_BOTTOM:
            default:
                return marginPx / mHeight;
        }
    }

    public GlWatermarkFilter setPosition(Position position) {
        this.position = position;
        return this;
    }

    public enum Position {
        LEFT_TOP,
        CENTER_TOP,
        RIGHT_TOP,
        LEFT_CENTER,
        CENTER,
        RIGHT_CENTER,
        LEFT_BOTTOM,
        CENTER_BOTTOM,
        RIGHT_BOTTOM
    }
}
