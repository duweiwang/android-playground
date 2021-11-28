package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.loading.XCommonLoadingLayout;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/3/27  10:21
 **/
@PageInfo(description = "loading集合", navigationId = R.id.fragment_loading)
public class LoadingFragment extends BaseSupportFragment {
    private XCommonLoadingLayout xCommonLoadingLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xCommonLoadingLayout = view.findViewById(R.id.xloading);
        xCommonLoadingLayout.startRefresh();
    }
}
