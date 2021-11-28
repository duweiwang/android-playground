package com.example.wangduwei.demos.gles.shape.rectangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/1
 */

public class RectangleView extends GLSurfaceView {

    private GLSurfaceView.Renderer mRender;
    public RectangleView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        mRender = new RectangleRender();
        setRenderer(mRender);
    }

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);

        mRender = new RectangleRender();
        setRenderer(mRender);
    }
}
