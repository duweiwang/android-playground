package com.example.wangduwei.demos.gles.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.lib_gles.render.OpenGLRender;


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
