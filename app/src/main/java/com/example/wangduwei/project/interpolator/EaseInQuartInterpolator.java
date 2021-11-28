package com.example.wangduwei.project.interpolator;

import android.animation.TimeInterpolator;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2016 UCWEB Corporation. All rights reserved
 * File        :
 * Description :
 * Creation    : 2016/7/26
 * Author      : kangyong.lky@alibaba-inc.com
 * History     : Creation, 2016/7/26, kangyong.lky, Create the file
 * ****************************************************************************
 */
public class EaseInQuartInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        return input * input * input * input;
    }

}