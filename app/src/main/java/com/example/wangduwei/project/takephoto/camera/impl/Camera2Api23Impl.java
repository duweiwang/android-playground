package com.example.wangduwei.project.takephoto.camera.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.params.StreamConfigurationMap;

import com.example.wangduwei.project.takephoto.camera.ICamera;
import com.example.wangduwei.project.takephoto.camera.Size;
import com.example.wangduwei.project.takephoto.camera.SizeMap;
import com.example.wangduwei.project.takephoto.preview.IPreview;


/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/9
 */
@TargetApi(23)
public class Camera2Api23Impl extends Camera2Impl {

    public Camera2Api23Impl(ICamera.CameraStateCallback callback, IPreview preview, Context context) {
        super(callback, preview,context);
    }

    @Override
    protected void collectPictureSizes(SizeMap sizes, StreamConfigurationMap map) {
        // Try to get hi-res output sizes
        android.util.Size[] outputSizes = map.getHighResolutionOutputSizes(ImageFormat.JPEG);
        if (outputSizes != null) {
            for (android.util.Size size : map.getHighResolutionOutputSizes(ImageFormat.JPEG)) {
                sizes.add(new Size(size.getWidth(), size.getHeight()));
            }
        }
        if (sizes.isEmpty()) {
            super.collectPictureSizes(sizes, map);
        }
    }


}
