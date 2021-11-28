package com.example.wangduwei.demos.gles.shape.triangle;

import android.opengl.GLSurfaceView;

import com.example.wangduwei.demos.gles.shape.IShape;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class TriangleRender implements GLSurfaceView.Renderer {

    private IShape mTriangle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1f, 0f, 0f, 0f);
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }
}
