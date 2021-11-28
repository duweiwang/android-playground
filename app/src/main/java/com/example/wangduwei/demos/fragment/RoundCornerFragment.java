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
 * @desc: 圆角的实现方式
 * @auther:duwei
 * @date:2018/8/30
 */
@PageInfo(description = "圆角实现", navigationId = R.id.fragment_view_round_corner)
public class RoundCornerFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.round_corner_layout, null);
    }

}
