package com.example.wangduwei.demos.main.appinfo.display;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.AbsBusinessDelegate;

/**
 * 屏幕信息相关
 */
public class DisplayInfoDelegate extends AbsBusinessDelegate implements IDisplayInfoView {

    private TextView mInfoText;
    private DisplayInfoPresenter mPresenter;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        mPresenter = new DisplayInfoPresenter(this);

        mInfoText = view.findViewById(R.id.info_dimension);
        //屏幕信息
        mInfoText.setText(mPresenter.getDisplayInfoString(view.getContext()));
    }

    @Override
    public void onDestroy() {

    }
}
