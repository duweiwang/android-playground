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
import com.example.wangduwei.demos.view.event.log.LogEventGroup;
import com.example.wangduwei.demos.view.event.log.LogEventView;

/**
 * @desc: 事件分发日志观察类
 * @auther:duwei
 * @date:2019/3/18
 */
@PageInfo(description = "事件分发日志",navigationId = R.id.fragment_event_log_out)
public class LogEventFragment extends BaseSupportFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogEventView log = new LogEventView(container.getContext());
        log.setBackgroundColor(Color.RED);

        LogEventGroup group = new LogEventGroup(container.getContext());
        group.setBackgroundColor(Color.BLUE);

        group.addView(log, new FrameLayout.LayoutParams(200, 200));

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
