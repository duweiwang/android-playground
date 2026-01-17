package com.example.wangduwei.demos.gles.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.lib_gles.texture.OpenGLRender7;

/**
 * 这个是空气曲棍球游戏的绘制-第七章 纹理贴图
 */
public class OpenGLTexture_View extends GLSurfaceView {
    private Renderer mRender;

    public OpenGLTexture_View(Context context) {
        this(context,null);
    }

    public OpenGLTexture_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        mRender = new OpenGLRender7(context);
        setRenderer(mRender);
    }
}
