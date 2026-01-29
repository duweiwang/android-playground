package com.example.lib_gles.texture;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 这个是空气曲棍球游戏的绘制-第七章 纹理贴图
 */
public class OpenGLImageTextureGLView extends GLSurfaceView {
    private Renderer mRender;

    public OpenGLImageTextureGLView(Context context) {
        this(context,null);
    }

    public OpenGLImageTextureGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        mRender = new OpenGLTextureRender(context);
        setRenderer(mRender);
    }
}
