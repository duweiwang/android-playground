package com.example.wangduwei.demos.fragment;

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
import com.example.wangduwei.demos.view.event.InnerIntercepte.Group;
import com.example.wangduwei.demos.view.event.InnerIntercepte.InnerView;

/**
 * @desc: 内部拦截法的demo演示
 * @auther:duwei
 * @date:2019/3/18
 */
@PageInfo(description = "内部拦截法",navigationId = R.id.fragment_event_inner_intercept)
public class InnerInterceptFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //内部Viwe
        InnerView innerView = new InnerView(container.getContext());
        innerView.setBackgroundColor(Color.RED);
        //外层View
        Group group = new Group(container.getContext());
        group.setBackgroundColor(Color.BLUE);

        group.addView(innerView, new FrameLayout.LayoutParams(200, 200));

        FrameLayout frameLayout = new FrameLayout(container.getContext());
        frameLayout.setBackgroundColor(Color.GREEN);
        frameLayout.addView(group, new FrameLayout.LayoutParams(500, 500));

        return frameLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
