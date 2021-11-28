package com.example.wangduwei.demos.main.appinfo.network;

import android.view.View;
import android.widget.TextView;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.AbsBusinessDelegate;

public class NetWorkInfoDelegate extends AbsBusinessDelegate {
    private TextView mTextView;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        mTextView = view.findViewById(R.id.info_network);
        mTextView.setText("");
    }

    @Override
    public void onDestroy() {

    }
}
