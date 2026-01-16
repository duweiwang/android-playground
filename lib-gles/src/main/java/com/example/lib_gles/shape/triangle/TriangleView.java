package com.example.lib_gles.shape.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 使用 OpenGL ES 绘制三角形
 */
public class TriangleView extends GLSurfaceView {
    GLSurfaceView.Renderer renderer;

    public TriangleView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new TriangleRender();
        setRenderer(renderer);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        renderer = new TriangleRender();
        setRenderer(renderer);
    }
}
