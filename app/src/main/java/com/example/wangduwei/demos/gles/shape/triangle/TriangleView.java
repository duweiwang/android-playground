package com.example.wangduwei.demos.gles.shape.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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
