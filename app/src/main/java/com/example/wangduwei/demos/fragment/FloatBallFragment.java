package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.floatview.impl1.CircleCountDownWrapperLayout;
import com.example.customview.floatview.ICountDown;
import com.example.customview.floatview.ICountDownFinishListener;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/8/13  16:45
 **/
@PageInfo(description = "拖拽浮球", navigationId = R.id.fragment_floatball)
public class FloatBallFragment extends BaseSupportFragment implements ICountDownFinishListener {

    ICountDown iCountDown;
    CircleCountDownWrapperLayout circleCountDownWrapperLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floatball, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.start_timedown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCountDown.start();
            }
        });
        view.findViewById(R.id.start_expand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleCountDownWrapperLayout.startAnimation();
            }
        });
        circleCountDownWrapperLayout = view.findViewById(R.id.circlelayout);
        iCountDown = circleCountDownWrapperLayout.getCircleView();
        iCountDown.setFinishListener(this);
        iCountDown.setShowProgress(true);
        iCountDown.setCountDownTime(1);

    }

    @Override
    public void onFinish() {
        Log.d("wdw", "倒计时结束...");
    }
}
