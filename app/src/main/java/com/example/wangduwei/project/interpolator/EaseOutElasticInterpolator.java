package com.example.wangduwei.project.interpolator;

import android.view.animation.Interpolator;

public class EaseOutElasticInterpolator implements Interpolator {
    
    @Override
    public float getInterpolation(float input) {
        float amplitude = 0.3f;
        float period = 0.3f;
        float p = easeOutElastic_helper(input, 0, 1, 1, amplitude, period);
        return p;
    }
    
    final float M_PI = 3.14159265358979323846F;
    final float M_PI_2 = M_PI / 2;
    float easeOutElastic_helper(float t, float b, float c, float d, float a, float p) {
        if (t==0) return 0;
        if (t==1) return c;
        
        float s;
        if(a < c) {
            a = c;
            s = p / 4.0f;
        } else {
            s = (float) (p / (2 * M_PI) * Math.asin(c / a));
        }
        
        return (float) (a* Math.pow(2.0f,-10*t) * Math.sin( (t-s)*(2*M_PI)/p ) + c);
    }
}
