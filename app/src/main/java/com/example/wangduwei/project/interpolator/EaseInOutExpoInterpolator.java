package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseInOutExpoInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        if (t == 0 || t == 1) {
            return t;
        }
        t *= 2.0f;
        if (t < 1) {
            return (float) (0.5f * Math.pow(2, 10 * (t - 1)) - 0.0005);
        }
        return (float) (0.5f * 1.0005 * (-Math.pow(2f, -10 * (t - 1)) + 2));
    }
    
}
