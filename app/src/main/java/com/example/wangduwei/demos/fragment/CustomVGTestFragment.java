package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.customview.viewgroup.ExpandableVG;

/**
 * <p>多个ViewGroup放在这里</p>
 *
 * @auther: davewang
 * @since: 2019/6/25
 **/
public class CustomVGTestFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_viewgroup, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExpandableVG) view.findViewById(R.id.ep)).toggle();
            }
        });
    }

}
