/**
 *****************************************************************************
 * Copyright (C) 2005-2016 UCWEB Corporation. All rights reserved
 * File        : EaseInOutQuintInterporator.java
 *
 * Description :
 *
 * Creation    : 2016-1-8
 * Author      : zhuoxu.lzx@alibaba-inc.com
 * History     :
 *****************************************************************************
 */
package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseInOutQuintInterporator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        input*=2.0;
        if (input < 1) {
            return (float) 0.5*input*input*input*input*input;
        } else {
            input -= 2.0;
            return (float) 0.5*(input*input*input*input*input + 2);
        }
    }
}
