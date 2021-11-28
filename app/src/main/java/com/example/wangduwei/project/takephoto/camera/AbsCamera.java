package com.example.wangduwei.project.takephoto.camera;

import android.view.View;

import com.example.wangduwei.project.takephoto.preview.IPreview;


/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/9
 */

public abstract class AbsCamera implements ICamera {

    protected final CameraStateCallback mCallback;
    protected final IPreview mPreview;

    public AbsCamera(CameraStateCallback callback, IPreview preview) {
        mCallback = callback;
        mPreview = preview;
    }

    @Override
    public View getView() {
        return mPreview.getView();
    }
}
