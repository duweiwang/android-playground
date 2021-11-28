package com.example.wangduwei.demos.view.customview.matrix;

import android.graphics.Matrix;

/**
 * @author : wangduwei
 * @date : 2020/3/3
 * @description :
 */
public interface IMatrixProcessor {

    void resetMatrix();

    Matrix getMatrix();

    /**
     * @param x x移动的距离
     * @param y y移动的距离
     */
    void setTranslate(float x, float y);
    void preTranslate(float x, float y);
    void postTranslate(float x, float y);

    /**
     * @param scaleX 旋转角度
     * @param scaleY 旋转角度
     * @param pivotX 旋转中心X
     * @param pivotY 旋转中心Y
     */
    void setScale(float scaleX, float scaleY, float pivotX, float pivotY);
    void preScale(float scaleX, float scaleY, float pivotX, float pivotY);
    void postScale(float scaleX, float scaleY, float pivotX, float pivotY);


    boolean preRotate(int degrees);
    boolean postRotate(int degrees);

}
