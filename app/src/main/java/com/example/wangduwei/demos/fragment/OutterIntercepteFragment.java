package com.example.wangduwei.demos.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.view.event.outterintercepte.Group;
import com.example.wangduwei.demos.view.event.outterintercepte.InnerView;

/**
 * @author :duwei
 * @desc: 外部拦截法Demo
 * @date:2019/3/19
 */
@PageInfo(description = "外部拦截法", navigationId = R.id.fragment_event_out_intercept)
public class OutterIntercepteFragment extends BaseSupportFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = container.getContext();

        InnerView view = new InnerView(context);
        view.setBackgroundColor(Color.RED);

        Group group = new Group(context);
        group.setBackgroundColor(Color.BLUE);
        group.addView(view, new FrameLayout.LayoutParams(200, 200));

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Color.GREEN);
        frameLayout.addView(group, new FrameLayout.LayoutParams(500, 500));

        return frameLayout;
    }
}
