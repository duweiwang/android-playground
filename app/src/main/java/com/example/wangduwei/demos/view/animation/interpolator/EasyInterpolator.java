package com.example.wangduwei.demos.view.animation.interpolator;

import android.animation.TimeInterpolator;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/13  10:43
 **/
public class EasyInterpolator implements TimeInterpolator {
    private final @Ease int ease;

    public EasyInterpolator(@Ease int ease) {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input) {
        return EasingProvider.get(this.ease, input);
    }

    public @Ease int getEase() {
        return ease;
    }
}
