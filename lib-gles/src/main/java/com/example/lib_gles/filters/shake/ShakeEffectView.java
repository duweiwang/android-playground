package com.example.lib_gles.filters.shake;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.example.lib_gles.filters.base.BaseGLFilterView;

/**
 * Shake Effect View - 抖动特效View
 * 通过缩放和RGB通道偏移实现抖动效果
 * 继承 BaseGLFilterView，复用通用逻辑
 */
public class ShakeEffectView extends BaseGLFilterView {

    public ShakeEffectView(Context context) {
        super(context);
    }

    public ShakeEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BaseRenderer createRenderer(Context context) {
        return new ShakeEffectRenderer(context);
    }

    /**
     * 抖动效果渲染器
     */
    private static class ShakeEffectRenderer extends BaseRenderer {
        
        private int mTextureCoordOffsetHandle;
        private float[] mScaleMatrix = new float[16];

        public ShakeEffectRenderer(Context context) {
            super(context);
        }

        @Override
        protected String getVertexShader() {
            return "soulout_v.glsl";
        }

        @Override
        protected String getFragmentShader() {
            return "shakeeffect_f.glsl";
        }

        @Override
        protected void onInit() {
            super.onInit();
            // 获取特效专用的 uniform
            mTextureCoordOffsetHandle = GLES30.glGetUniformLocation(mProgramId, "uTextureCoordOffset");
            android.util.Log.d("ShakeEffectView", "TextureCoordOffset handle: " + mTextureCoordOffsetHandle);
        }

        @Override
        protected void onDrawArraysPre() {
            // 计算缩放：1.0 -> 1.2
            float scale = 1.0f + 0.2f * mProgress;
            Matrix.setIdentityM(mScaleMatrix, 0);
            Matrix.scaleM(mScaleMatrix, 0, scale, scale, 1.0f);
            
            // 计算纹理坐标偏移量
            float textureCoordOffset = 0.01f * mProgress;
            
            // 设置uniform变量
            GLES30.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mScaleMatrix, 0);
            GLES30.glUniform1f(mTextureCoordOffsetHandle, textureCoordOffset);
        }
    }
}
