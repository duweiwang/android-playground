package com.example.wangduwei.demos.performance.memory;

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
 * <p>用于制造内存泄露，可以触发LeakCanary警告
 *
 * @auther : wangduwei
 * @since : 2019/9/6  17:25
 **/
@PageInfo(description = "内存泄露", navigationId = R.id.fragment_performance)
public class LeakTestFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_performance, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.leak_inner_class).setOnClickListener(this);
        view.findViewById(R.id.leak_animator).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.leak_inner_class:
                LeakedTargetActivity.start(getActivity(),LeakedTargetActivity.FROM_INNER_CLASS_LEAK);
                break;
            case R.id.leak_animator:
                LeakedTargetActivity.start(getActivity(),LeakedTargetActivity.FROM_ANIMATOR_LEAK);
                break;
        }
    }
}
