package com.example.wangduwei.demos.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customview.surfaceview.FireflySurfaceView;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * <p>
 * 浮点粒子效果--SurfaceView子线程绘制主线程更新UI
 * </p>
 *
 * @auther:duwei
 * @date:2019/2/22
 */
@PageInfo(description = "浮点粒子效果", navigationId = R.id.fragment_surface_particle)
public class ParticleSurfaceFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FireflySurfaceView fireflyView = new FireflySurfaceView(container.getContext(), null);
        fireflyView.setBackgroundResource(R.mipmap.ic_firefly_bg);
        return fireflyView;
    }
}
