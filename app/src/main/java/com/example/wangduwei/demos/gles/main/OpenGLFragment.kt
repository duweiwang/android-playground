package com.example.wangduwei.demos.gles.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_gles.glitch.GlitchEffectView
import com.example.lib_gles.shake.ShakeEffectView
import com.example.lib_gles.soulout.SoulOutView
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

@PageInfo(
    description = "使用OpenGL绘制基本的图形",
    navigationId = R.id.fragment_gl_basic,
    title = "OpenGl画图",
    preview = R.drawable.preview_opengl
)
class OpenGLFragment : BaseSupportFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_opengl_shape, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val soulOutView = view.findViewById<SoulOutView>(R.id.soul_out_view)
        soulOutView.setImageResource(R.drawable.img)
        soulOutView.setMaxFrames(60)  // 动画60帧完成（约1秒）
        soulOutView.setSkipFrames(30) // 延迟30帧后重复


        val shakeEffectView = view.findViewById<ShakeEffectView>(R.id.shake_effect_view)
        shakeEffectView.setImageResource(R.drawable.img)  // 设置图片
        shakeEffectView.setMaxFrames(60)   // 动画60帧（约1秒）
        shakeEffectView.setSkipFrames(30)  // 延迟30帧后重复


        val glitchView = view.findViewById<GlitchEffectView>(R.id.glitch_view)
        glitchView.setImageResource(R.drawable.img)
    }
}
