package com.example.wangduwei.demos.gles.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.wangduwei.demos.gles.fbo.Test7Renderer;
import com.example.wangduwei.demos.gles.render.OpenGLRender;
import com.example.wangduwei.demos.gles.texture.OpenGLRender7;

public class OpenGLView extends GLSurfaceView {
    private GLSurfaceView.Renderer mRender;

    public OpenGLView(Context context) {
        this(context,null);
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
//        mRender = new Test7Renderer(context);
//        mRender = new OpenGLRender7(context);
        mRender = new OpenGLRender(context);
        setRenderer(mRender);
    }
}
