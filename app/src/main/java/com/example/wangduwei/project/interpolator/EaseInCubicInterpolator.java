package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseInCubicInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        return t*t*t;
    }

}
