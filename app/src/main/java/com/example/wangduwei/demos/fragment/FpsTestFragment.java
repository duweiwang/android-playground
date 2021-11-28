package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.functions.fps_calculator.FpsCalculator;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc: 实时显示当前帧率，Choreographer.FrameCallback实现
 * @auther:duwei
 * @date:2019/1/30
 */
@PageInfo(description = "帧率计算", navigationId = R.id.fragment_performance_fps)
public class FpsTestFragment extends BaseSupportFragment {

    FpsCalculator fpsTask;

    public static FpsTestFragment newInstance(){
        return new FpsTestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pfs,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.fps_text);
        fpsTask = new FpsCalculator(text);
    }

    @Override
    public void onResume() {
        super.onResume();
        fpsTask.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        fpsTask.stop();
    }
}
