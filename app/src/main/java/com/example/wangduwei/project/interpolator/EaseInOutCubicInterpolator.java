/*
 * Copyright (C) 2004 - 2016 UCWeb Inc. All Rights Reserved.
 * Description : EaseInOutCubicInterpolator.java
 *
 * Creation    : 2016/02/16
 * Author      : weihan.hwh@alibaba-inc.com
 */

package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseInOutCubicInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return getValue(input);
    }

    public static float getValue(float input) {
        return input < 0.5f ?
                4 * input * input * input :
                (input - 1) * (2 * input - 2) * (2 * input - 2) + 1;
    }
}
