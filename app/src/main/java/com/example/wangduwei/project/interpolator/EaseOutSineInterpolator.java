package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutSineInterpolator implements Interpolator {

    private static final float PI = 3.14159f;

    @Override
    public float getInterpolation(float t) {
        return (float) Math.sin(t*PI/2);
    }

}
