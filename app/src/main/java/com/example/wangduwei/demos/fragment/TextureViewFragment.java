package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextureViewDemo textureViewDemo = new TextureViewDemo(container.getContext());

        linearLayout.addView(textureViewDemo, lp);
        return linearLayout;
    }
}
