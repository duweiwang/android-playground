package com.example.wangduwei.demos.main;

import android.view.View;

/**
 * Fragment业务代理 基类
 */
public abstract class AbsBusinessDelegate {

    public View mView;

    public void onViewCreated(View view) {
        mView = view;
    }



    public abstract void onDestroy();
}
