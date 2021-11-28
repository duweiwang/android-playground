package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.basic_api.WealthGodNumberViewGroup;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.util.Random;

/**
 * @auther: davewang
 * @since: 2019/7/4
 **/
@PageInfo(description = "数字滚动控件",navigationId = R.id.fragment_view_scroll_number)
public class ScrollNumFragment extends BaseSupportFragment implements WealthGodNumberViewGroup.IAnimationEndListener {

    WealthGodNumberViewGroup wealthGodNumberViewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll_num, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wealthGodNumberViewGroup = view.findViewById(R.id.WealthGodNumberViewGroup);
        wealthGodNumberViewGroup.setOnAnimationEndListener(this);
        view.findViewById(R.id.start).setOnClickListener(this);
        view.findViewById(R.id.end).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.start) {
            wealthGodNumberViewGroup.start();
        } else if (view.getId() == R.id.end) {
            Random random = new Random();
            wealthGodNumberViewGroup.setValueAndStop(random.nextInt(9999));
        }
    }

    @Override
    public void onAnimationEnd() {
    }
}
