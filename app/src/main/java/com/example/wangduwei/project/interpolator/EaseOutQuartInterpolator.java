/*
 * Copyright (C) 2004 - 2016 UCWeb Inc. All Rights Reserved.
 * Description : EaseOutQuartInterpolator.java
 *
 * Creation    : 2016/02/16
 * Author      : weihan.hwh@alibaba-inc.com
 */

package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutQuartInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return getValue(input);
    }

    public static float getValue(float input) {
        return 1 - (--input) * input * input * input;
    }
}
