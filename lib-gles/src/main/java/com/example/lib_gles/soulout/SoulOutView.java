package com.example.lib_gles.soulout;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.example.lib_gles.base.BaseGLFilterView;

/**
 * SoulOut Effect View - 灵魂出窍特效View
 * 继承 BaseGLFilterView，复用通用逻辑
 */
public class SoulOutView extends BaseGLFilterView {

    public SoulOutView(Context context) {
        super(context);
    }

    public SoulOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BaseRenderer createRenderer(Context context) {
        return new SoulOutRenderer(context);
    }

    /**
     * 灵魂出窍渲染器
     */
    private static class SoulOutRenderer extends BaseRenderer {
        
        private int mAlphaHandle;
        private float[] mScaleMatrix = new float[16];

        public SoulOutRenderer(Context context) {
            super(context);
        }

        @Override
        protected String getVertexShader() {
            return "soulout_v.glsl";
        }

        @Override
        protected String getFragmentShader() {
            return "soulout_f.glsl";
        }

        @Override
        protected void onInit() {
            super.onInit();
            // 获取特效专用的 uniform
            mAlphaHandle = GLES30.glGetUniformLocation(mProgramId, "uAlpha");
            android.util.Log.d("SoulOutView", "Alpha handle: " + mAlphaHandle);
        }

        @Override
        protected void onDrawArraysPre() {
            // 启用混合（灵魂出窍需要叠加两层）
            GLES30.glEnable(GLES30.GL_BLEND);
            GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_DST_ALPHA);
            
            // 第一次绘制：正常图像（背景层）
            float backAlpha = 1.0f;
            if (mProgress > 0) {
                float alpha = 0.2f - mProgress * 0.2f;
                backAlpha = 1.0f - alpha;
            }
            Matrix.setIdentityM(mScaleMatrix, 0);
            GLES30.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mScaleMatrix, 0);
            GLES30.glUniform1f(mAlphaHandle, backAlpha);
        }

        @Override
        protected void onDrawArrays() {
            // 第一次绘制
            GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
            
            // 第二次绘制：放大+透明的灵魂层
            if (mProgress > 0) {
                float alpha = 0.2f - mProgress * 0.2f;
                float scale = 1.0f + mProgress;
                
                Matrix.setIdentityM(mScaleMatrix, 0);
                Matrix.scaleM(mScaleMatrix, 0, scale, scale, 1.0f);
                
                GLES30.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mScaleMatrix, 0);
                GLES30.glUniform1f(mAlphaHandle, alpha);
                GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
            }
        }

        @Override
        protected void onDrawArraysAfter() {
            // 禁用混合
            GLES30.glDisable(GLES30.GL_BLEND);
        }
    }
}
