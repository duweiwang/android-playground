package com.example.wangduwei.project.takephoto.camera;

import android.view.View;

import java.util.Set;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/8
 */

public interface ICamera {

    interface CameraStateCallback {
        void onCameraOpened();

        void onCameraClosed();

        void onPictureTaken(byte[] data);
    }


    View getView();

    /**
     * @return {@code true} if the implementation was able to start the camera session.
     */
    boolean start();

    void stop();

    boolean isCameraOpened();

    void setFacing(int facing);

    int getFacing();

    Set<AspectRatio> getSupportedAspectRatios();

    /**
     * @return {@code true} if the aspect ratio was changed.
     */
    boolean setAspectRatio(AspectRatio ratio);

    AspectRatio getAspectRatio();

    void setAutoFocus(boolean autoFocus);

    boolean getAutoFocus();

    void setFlash(int flash);

    int getFlash();

    void takePicture();

    void setDisplayOrientation(int displayOrientation);

}
