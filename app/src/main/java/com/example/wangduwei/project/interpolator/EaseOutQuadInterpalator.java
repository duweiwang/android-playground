package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutQuadInterpalator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        return -t*(t-2);
    }

}
