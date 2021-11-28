package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2016/4/14.
 */
public class EaseOutQuintInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float v) {
        float v2 = (--v) * v;
        return 1 + v * v2 * v2;
    }

}
