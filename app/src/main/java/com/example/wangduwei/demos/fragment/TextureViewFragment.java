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
import com.example.wangduwei.demos.view.TextureViewDemo;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/5/2
 */
@PageInfo(description = "TextureView与相机",navigationId = R.id.fragment_use_textureview)
public class TextureViewFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new TextureViewDemo(container.getContext());
    }
}
