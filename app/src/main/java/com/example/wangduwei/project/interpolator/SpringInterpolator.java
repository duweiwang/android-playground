package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

/**
 * @desc: 阻尼插值器
 * @auther:duwei
 * @date:2018/12/26
 */

public class SpringInterpolator implements Interpolator {
    private float mFactor = 0.4f;
    public SpringInterpolator(float factor) {
        mFactor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - mFactor / 6) * (3 * Math.PI) / mFactor) + 1);
    }
}
