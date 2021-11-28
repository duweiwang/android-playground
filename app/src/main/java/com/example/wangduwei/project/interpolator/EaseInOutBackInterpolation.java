/*
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description :
 *
 * Creation    :  2016-03-25
 * Author      : Administrator
 */

package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public  class EaseInOutBackInterpolation implements Interpolator {

    @Override
    public  float  getInterpolation(float  t)  {
        return  (float)  getInterpolation(t,  0,  1,  1,  1.70158f);
    }

    public  static  double  getInterpolation(float  t,  float  b,  float  c,  float  d,  float  s)  {
        if  ((t  /=  d  /  2)  <  1)  return  c  /  2  *  (t  *  t  *  (((s  *=  (1.525))  +  1)  *  t  -  s))  +  b;
        return  c  /  2  *  ((t  -=  2)  *  t  *  (((s  *=  (1.525))  +  1)  *  t  +  s)  +  2)  +  b;
    }
}