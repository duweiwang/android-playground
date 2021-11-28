package com.example.wangduwei.demos.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.basic_api.BazierView;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

@PageInfo(description = "贝塞尔曲线", navigationId = R.id.fragment_view_bezier_demo)
public class BezierTestFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BazierView bazierView = new BazierView(getActivity());
        bazierView.setBackgroundColor(Color.WHITE);
        return bazierView;
    }
}
