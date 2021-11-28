package com.example.wangduwei.demos.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.view.nestedscroll.androideventdispatch.NestedChildView;
import com.example.wangduwei.demos.view.nestedscroll.androideventdispatch.NestedParentView;


/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/12
 */
@PageInfo(description = "嵌套滚动", navigationId = R.id.fragment_event_nested_scroll_demo)
public class NestedScrollFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout.LayoutParams parentLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                500);
        parentLp.gravity = Gravity.BOTTOM;
        NestedParentView nestedParentView = new NestedParentView(getActivity());
        nestedParentView.setBackgroundColor(Color.RED);

        FrameLayout.LayoutParams childLp = new FrameLayout.LayoutParams(100, 100);
        childLp.gravity = Gravity.BOTTOM;
        NestedChildView nestedChildView = new NestedChildView(getActivity());
        nestedChildView.setBackgroundColor(Color.BLACK);


        nestedParentView.addView(nestedChildView, childLp);

        FrameLayout fr = new FrameLayout(getActivity());
        fr.addView(nestedParentView, parentLp);
        return fr;
    }
}
