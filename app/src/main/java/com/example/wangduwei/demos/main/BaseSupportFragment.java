package com.example.wangduwei.demos.main;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

/**
 * @desc: 解决事件透传问题，避免下层的Fragment收到事件
 * 1.在Fragment跟布局添加clickable = true
 * 2.实现View.OnTouchListener
 * 3.实现View.OnclickListener
 * @auther:duwei
 * @date:2019/3/12
 */
public class BaseSupportFragment extends BaseVisibleFragment implements View.OnClickListener {

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        view.setOnClickListener(this);
    }
}
