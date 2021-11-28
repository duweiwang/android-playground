/*
 * Copyright (C) 2004 - 2016 UCWeb Inc. All Rights Reserved.
 * Description : EaseInOutQuartInterpolator.java
 *
 * Creation    : 2016/02/16
 * Author      : weihan.hwh@alibaba-inc.com
 */

package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseInOutQuartInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return getValue(input);
    }

    public static float getValue(float input) {
        return input < 0.5f ? 8 * input * input * input * input : 1 - 8 * (--input) * input * input * input;
    }
}
