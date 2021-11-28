package com.example.wangduwei.demos.floatwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wangduwei.demos.AppState;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/9
 */

public class FloatWindowFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_float_window,null);
    }

    public void show(View v){
        TipViewController tip = new TipViewController(AppState.INSTANCE.getContext(),"8888888888");
        tip.setViewDismissHandler(new TipViewController.ViewDismissHandler() {
            @Override
            public void onViewDismiss() {
                Toast.makeText(getActivity(), "onViewDismiss", Toast.LENGTH_SHORT).show();
            }
        });
        tip.show();
    }
}

