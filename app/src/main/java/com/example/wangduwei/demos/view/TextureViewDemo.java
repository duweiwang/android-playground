package com.example.wangduwei.demos.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/5/2
 *
 * 使用TextureView预览相机
 *
 *
 */

public class TextureViewDemo extends TextureView implements TextureView.SurfaceTextureListener{
    private Camera mCamera;
    public TextureViewDemo(Context context) {
        super(context);
        setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        // TODO: 2019/6/24 权限
        mCamera = android.hardware.Camera.open();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        setLayoutParams(new FrameLayout.LayoutParams(previewSize.width,previewSize.height, Gravity.CENTER));
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        setAlpha(1.0f);
        setRotation(45.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
