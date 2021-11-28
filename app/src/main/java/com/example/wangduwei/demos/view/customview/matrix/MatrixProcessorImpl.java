package com.example.wangduwei.demos.view.customview.matrix;

import android.graphics.Matrix;

/**
 * @author : wangduwei
 * @date : 2020/3/3
 * @description :
 */
public class MatrixProcessorImpl implements IMatrixProcessor {

    private final Matrix mMatrix = new Matrix();

    public MatrixProcessorImpl() {

    }

    @Override
    public void resetMatrix() {
        mMatrix.reset();
    }

    @Override
    public Matrix getMatrix() {
        return mMatrix;
    }

    @Override
    public void setTranslate(float x, float y) {
        mMatrix.setTranslate(x, y);
    }

    @Override
    public void preTranslate(float x, float y) {
        mMatrix.preTranslate(x, y);
    }

    public void postTranslate(float x, float y) {
        mMatrix.postTranslate(x, y);
    }

    @Override
    public void setScale(float scaleX, float scaleY, float pivotX, float pivotY) {
        mMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
    }

    @Override
    public void preScale(float scaleX, float scaleY, float pivotX, float pivotY) {
        mMatrix.preScale(scaleX, scaleY, pivotX, pivotY);
    }

    @Override
    public void postScale(float scaleX, float scaleY, float pivotX, float pivotY) {
        mMatrix.postScale(scaleX, scaleY, pivotX, pivotY);
    }

    @Override
    public boolean preRotate(int degrees) {
        return mMatrix.preRotate(degrees);
    }

    @Override
    public boolean postRotate(int degrees) {
        return mMatrix.postRotate(degrees);
    }
}
