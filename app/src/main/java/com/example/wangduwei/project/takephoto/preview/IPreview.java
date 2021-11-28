package com.example.wangduwei.project.takephoto.preview;

import android.view.Surface;
import android.view.View;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/8
 */

public interface IPreview {
    boolean isReady();

    Surface getSurface();

    Object getSurfaceTexture();

    View getView();

    Class getOutputClass();

    void setDisplayOrientation(int displayOrientation);

    void setSurfaceChangeCallback(SurfaceChangeCallback callback);

    void setBufferSize(int width, int height);

    int getWidth();

    int getHeight();


    interface SurfaceChangeCallback {
        void onSurfaceChanged();
    }
}
