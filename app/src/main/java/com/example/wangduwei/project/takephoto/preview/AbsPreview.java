package com.example.wangduwei.project.takephoto.preview;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/8
 */

public abstract class AbsPreview implements IPreview {

    private int mWidth;

    private int mHeight;
    private SurfaceChangeCallback mCallback;

    protected void dispatchSurfaceChanged() {
        mCallback.onSurfaceChanged();
    }

    @Override
    public void setSurfaceChangeCallback(SurfaceChangeCallback callback) {
        mCallback = callback;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

}
