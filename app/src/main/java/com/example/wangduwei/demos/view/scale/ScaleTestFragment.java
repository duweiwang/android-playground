package com.example.wangduwei.demos.view.scale;

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
 * @desc:
 * @auther:duwei
 * @date:2019/3/7
 */
@PageInfo(description = "手势操作", navigationId = R.id.fragment_event_image_scale)
public class ScaleTestFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new GestureScaleImageView(getActivity());
    }
}
