package com.example.lib_gles.glitch;

import android.content.Context;
import android.opengl.GLES30;
import android.util.AttributeSet;

import com.example.lib_gles.base.BaseGLFilterView;

/**
 * Glitch Effect View - 毛刺特效View
 * 通过扫描线抖动和颜色漂移实现故障艺术效果
 * 继承 BaseGLFilterView，复用通用逻辑
 */
public class GlitchEffectView extends BaseGLFilterView {

    public GlitchEffectView(Context context) {
        super(context);
    }

    public GlitchEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BaseRenderer createRenderer(Context context) {
        return new GlitchEffectRenderer(context);
    }

    /**
     * 毛刺效果渲染器
     */
    private static class GlitchEffectRenderer extends BaseRenderer {
        
        private int mScanLineJitterLocation;
        private int mColorDriftLocation;
        
        // 动画序列数据
        private float[] mDriftSequence;
        private float[] mJitterSequence;
        private float[] mThresholdSequence;
        
        // 当前帧索引（覆盖父类的 mFrames）
        private int mCurrentFrame = 0;
        private int mSequenceLength = 9;

        public GlitchEffectRenderer(Context context) {
            super(context);
        }

        @Override
        protected String getVertexShader() {
            return "nofilter_v.glsl";
        }

        @Override
        protected String getFragmentShader() {
            return "glitch_f.glsl";
        }

        @Override
        protected void onInit() {
            super.onInit();
            // 获取特效专用的 uniform
            mScanLineJitterLocation = GLES30.glGetUniformLocation(mProgramId, "uScanLineJitter");
            mColorDriftLocation = GLES30.glGetUniformLocation(mProgramId, "uColorDrift");
            android.util.Log.d("GlitchEffectView", "ScanLineJitter: " + mScanLineJitterLocation + ", ColorDrift: " + mColorDriftLocation);
        }

        @Override
        protected void onInitialized() {
            super.onInitialized();
            // 初始化动画序列（来自 C++ 代码）
            mDriftSequence = new float[]{0.0f, 0.03f, 0.032f, 0.035f, 0.03f, 0.032f, 0.031f, 0.029f, 0.025f};
            mJitterSequence = new float[]{0.0f, 0.03f, 0.01f, 0.02f, 0.05f, 0.055f, 0.03f, 0.02f, 0.025f};
            mThresholdSequence = new float[]{1.0f, 0.965f, 0.9f, 0.9f, 0.9f, 0.6f, 0.8f, 0.5f, 0.5f};
        }

        @Override
        protected void onDrawArraysPre() {
            // 首先设置 MVP 矩阵（毛刺效果使用单位矩阵）
            super.onDrawArraysPre();
            
            // 设置扫描线抖动参数（vec2：x=抖动值, y=阈值）
            GLES30.glUniform2f(mScanLineJitterLocation, 
                mJitterSequence[mCurrentFrame], 
                mThresholdSequence[mCurrentFrame]);
            
            // 设置颜色漂移参数
            GLES30.glUniform1f(mColorDriftLocation, mDriftSequence[mCurrentFrame]);
            
            // 更新帧索引
            mCurrentFrame++;
            if (mCurrentFrame >= mSequenceLength) {
                mCurrentFrame = 0;
            }
        }
        
        @Override
        protected void updateProgress() {
            // 重写父类方法，毛刺效果不需要基于时间的 progress
            // 直接使用序列索引即可
        }
    }
}
