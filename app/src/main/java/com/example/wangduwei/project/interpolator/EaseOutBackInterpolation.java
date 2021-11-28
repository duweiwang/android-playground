/*
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description :
 *
 * Creation    :  2016-03-25
 * Author      : Administrator
 */

package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public  class EaseOutBackInterpolation implements Interpolator {

    @Override
    public  float  getInterpolation(float  t)  {
        return (float) (1 + (--t) * t * (2.70158 * t + 1.70158));
    }
}