package com.example.lib_gles.fbo.fbo_3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 这个是空气曲棍球游戏的绘制
 */
public class Fbo3DCubeView extends GLSurfaceView {
    private Renderer mRender;

    public Fbo3DCubeView(Context context) {
        this(context,null);
    }

    public Fbo3DCubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        mRender = new Fbo3DCubeRender(context);
//        mRender = new OpenGLRender7(context);
        setRenderer(mRender);
    }
}
