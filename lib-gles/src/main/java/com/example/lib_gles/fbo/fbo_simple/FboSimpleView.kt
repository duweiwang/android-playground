package com.example.lib_gles.fbo.fbo_simple

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * 最简单的FBO演示
 */
class FboSimpleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : GLSurfaceView(context, attrs) {


    private var mRender: Renderer? = null

    init {
        setEGLContextClientVersion(2)
        mRender = FboRenderer()
        setRenderer(mRender)
    }


}
