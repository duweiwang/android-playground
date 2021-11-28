package com.example.wangduwei.project.interpolator;

import android.graphics.PointF;
import android.view.animation.Interpolator;

public class EaseCubicBezierInterpolator implements Interpolator {

    private final static int ACCURACY = 1000; 
    
    private final double[] mValueXs = new double[ACCURACY];
    
    private final PointF mControlPoint1 = new PointF();
    private final PointF mControlPoint2 = new PointF();

    public EaseCubicBezierInterpolator(float x1,
                                       float y1,
                                       float x2,
                                       float y2) {
        mControlPoint1.x = x1;
        mControlPoint1.y = y1;
        mControlPoint2.x = x2;
        mControlPoint2.y = y2;
        calucateValues();
    }

    private void calucateValues() {
        for (int i = 0; i < ACCURACY; i++) {
            float t = 1.0f * i / ACCURACY;
            double x = cubicCurves(t, 0, mControlPoint1.x, mControlPoint2.x, 1);
            mValueXs[i] = x;
        }
    }

    @Override
    public float getInterpolation(float input) {
        float t = input;
        for (int i = 0; i < mValueXs.length; i++) {
            if (mValueXs[i] >= input) {
                t = 1.0f * i / ACCURACY;
                break;
            }
        }
        double value = cubicCurves(t, 0, mControlPoint1.y, mControlPoint2.y, 1);
        if (value > 0.999d && value <= 1d) {
            value = 1;
        }
        return (float) value;
    }

    public static double cubicCurves(double t, double value0, double value1, double value2, double value3) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        double value = uuu * value0;
        value += 3 * uu * t * value1;
        value += 3 * u * tt * value2;
        value += ttt * value3;
        return value;
    }
}
