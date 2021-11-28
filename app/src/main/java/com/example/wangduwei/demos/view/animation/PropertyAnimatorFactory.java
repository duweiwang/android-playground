package com.example.wangduwei.demos.view.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.view.View;

/**
 * <p>Android属性动画集合
 *
 * @author : wangduwei
 * @since : 2019/12/23  19:30
 **/
public class PropertyAnimatorFactory {

    /**
     * 将View移动distanceX距离和distanceY距离
     * @param view
     * @param distanceX X方向的距离
     * @param distanceY Y方向的距离
     * @return
     */
    public static ObjectAnimator getTranslationAnimator(View view, int distanceX, int distanceY) {
        PropertyValuesHolder x = PropertyValuesHolder.
                ofFloat("translationX", view.getX(), view.getX() + distanceX);
        PropertyValuesHolder y = PropertyValuesHolder.
                ofFloat("translationY", view.getY(), view.getY() + distanceY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, x, y);
        return objectAnimator;
    }

    public static void configAnimatior(ObjectAnimator objectAnimator, long duration, int repeatCount,
                                       int repeatMode, TimeInterpolator interpolator) {
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(repeatCount);
        objectAnimator.setRepeatMode(repeatMode);
        objectAnimator.setInterpolator(interpolator);
    }
}
