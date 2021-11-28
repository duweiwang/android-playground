package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutQuintInterporator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        input -= 1.0;
        return input * input * input * input * input + 1;
    }
}