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
 * 让一个View跟着手指移动的多种实现方式
 * </p>
 *
 * @auther:duwei
 * @date:2018/2/2
 */
@PageInfo(description = "手指移动", navigationId = R.id.fragment_move_child_view_api)
public class FollowFingerFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow_finger_view, null);
    }
}
