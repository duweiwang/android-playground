package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * <p>
 * 演示Scroller的使用、目前包含ViewPager实现原理、手势下拉阻尼
 * </p>
 *
 * @auther:duwei
 * @date:2018/3/12
 */
@PageInfo(description = "Scroller使用",navigationId = R.id.fragment_scroller_demo)
public class ScrollerDemoFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroller_demo, null);

    }
}
