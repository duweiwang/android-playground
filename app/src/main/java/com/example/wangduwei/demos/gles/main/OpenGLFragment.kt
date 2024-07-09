package com.example.wangduwei.demos.gles.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}
