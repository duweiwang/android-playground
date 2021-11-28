package com.example.wangduwei.demos.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.utils.AppUtils;
import com.example.wangduwei.project.interpolator.EaseCubicBezierInterpolator;
import com.example.wangduwei.project.interpolator.EaseInCubicInterpolator;
import com.example.wangduwei.project.interpolator.EaseInOutBackInterpolation;
import com.example.wangduwei.project.interpolator.EaseInOutCubicInterpolator;
import com.example.wangduwei.project.interpolator.EaseInOutExpoInterpolator;
import com.example.wangduwei.project.interpolator.EaseInOutQuartInterpolator;
import com.example.wangduwei.project.interpolator.EaseInOutQuintInterporator;
import com.example.wangduwei.project.interpolator.EaseInQuartInterpolator;
import com.example.wangduwei.project.interpolator.EaseOutBackInterpolation;
import com.example.wangduwei.project.interpolator.EaseOutCubicInterpolator;
import com.example.wangduwei.project.interpolator.EaseOutElasticInterpolator;
import com.example.wangduwei.project.interpolator.EaseOutQuadInterpalator;
import com.example.wangduwei.project.interpolator.EaseOutQuartInterpolator;
import com.example.wangduwei.project.interpolator.EaseOutQuintInterpolator;
import com.example.wangduwei.project.interpolator.EaseOutSineInterpolator;
import com.example.wangduwei.project.interpolator.SpringInterpolator;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/8  14:31
 **/
@PageInfo(description = "插值器", navigationId = R.id.fragment_interpolator)
public class InterpolatorDemoFragment extends BaseSupportFragment implements RadioGroup.OnCheckedChangeListener {
    View ball;
    RadioGroup radioGroup;
    ObjectAnimator objectAnimator;
    Interpolator interpolator = new AccelerateInterpolator();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interpolator, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ball = view.findViewById(R.id.ball);
        radioGroup = view.findViewById(R.id.interpolator_radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        objectAnimator = ObjectAnimator.ofFloat(ball,
                "translationY", ball.getY(), ball.getY() + AppUtils.getScreenHeight() * 2 / 4);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(interpolator);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.anticipate:
                objectAnimator.setInterpolator(new AnticipateInterpolator());
                break;
            case R.id.bounce:
                objectAnimator.setInterpolator(new BounceInterpolator());
                break;
            case R.id.overshoot:
                objectAnimator.setInterpolator(new OvershootInterpolator());
                break;
            case R.id.spring:
                objectAnimator.setInterpolator(new SpringInterpolator(0.4f));
                break;
            case R.id.ease_out_sine:
                objectAnimator.setInterpolator(new EaseOutSineInterpolator());
                break;
            case R.id.ease_out_quint:
                objectAnimator.setInterpolator(new EaseOutQuintInterpolator());
                break;

            //--
            case R.id.ease_out_quart:
                objectAnimator.setInterpolator(new EaseOutQuartInterpolator());
                break;
            case R.id.ease_out_quad:
                objectAnimator.setInterpolator(new EaseOutQuadInterpalator());
                break;
            case R.id.ease_out_elastic:
                objectAnimator.setInterpolator(new EaseOutElasticInterpolator());
                break;
            case R.id.ease_out_cubic:
                objectAnimator.setInterpolator(new EaseOutCubicInterpolator());
                break;
            case R.id.ease_out_back:
                objectAnimator.setInterpolator(new EaseOutBackInterpolation());
                break;
            //---

            case R.id.ease_in_quart:
                objectAnimator.setInterpolator(new EaseInQuartInterpolator());
                break;

            case R.id.ease_in_OutQuint:
                objectAnimator.setInterpolator(new EaseInOutQuintInterporator());
                break;

            case R.id.ease_in_OutQuart:
                objectAnimator.setInterpolator(new EaseInOutQuartInterpolator());
                break;

            case R.id.ease_in_OutExpo:
                objectAnimator.setInterpolator(new EaseInOutExpoInterpolator());
                break;

            case R.id.ease_in_OutCubic:
                objectAnimator.setInterpolator(new EaseInOutCubicInterpolator());
                break;

            //--
            case R.id.ease_in_OutBack:
                objectAnimator.setInterpolator(new EaseInOutBackInterpolation());
                break;
            case R.id.ease_in_Cubic:
                objectAnimator.setInterpolator(new EaseInCubicInterpolator());
                break;
            case R.id.ease_CubicBezier:
                objectAnimator.setInterpolator(new EaseCubicBezierInterpolator(ball.getX() - 50, ball.getY(),
                        ball.getX() + 50, ball.getY()));
                break;
        }
        objectAnimator.start();
    }
}
