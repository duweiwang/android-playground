package com.example.wangduwei.demos.gles.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.lib_gles.fbo.Test7Renderer;
import com.example.lib_gles.render.OpenGLRender;

/**
 * 这个是空气曲棍球游戏的绘制
 */
public class OpenGL_FBOView extends GLSurfaceView {
    private Renderer mRender;

    public OpenGL_FBOView(Context context) {
        this(context,null);
    }

    public OpenGL_FBOView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        mRender = new Test7Renderer(context);
//        mRender = new OpenGLRender7(context);
        setRenderer(mRender);
    }
}
