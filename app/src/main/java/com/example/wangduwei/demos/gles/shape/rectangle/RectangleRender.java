package com.example.wangduwei.demos.gles.shape.rectangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.wangduwei.demos.gles.shape.IShape;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/1
 */

public class RectangleRender implements GLSurfaceView.Renderer {

    private IShape shape;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        shape = new Rectangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
