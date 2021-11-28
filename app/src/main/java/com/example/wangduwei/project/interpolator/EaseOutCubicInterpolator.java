package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutCubicInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        t -= 1.0f;
        return t*t*t + 1;
    }

}
