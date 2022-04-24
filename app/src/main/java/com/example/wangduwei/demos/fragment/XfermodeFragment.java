package com.example.wangduwei.demos.fragment;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customview.basic_api.XFermodeView;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/***
 * Xfermode演示
 */
@PageInfo(description = "演示图层混合模式的使用", navigationId = R.id.fragment_view_xfermode,title = "Xfermode",preview = R.drawable.preview_xfermode)
public class XfermodeFragment extends BaseSupportFragment {

    private XFermodeView xFermodeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_xfermode, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xFermodeView = view.findViewById(R.id.xfermode_view);
        view.findViewById(R.id.xfermode_btn1).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn2).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn3).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn4).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn5).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn6).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn7).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn8).setOnClickListener(this);
        view.findViewById(R.id.xfermode_btn9).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.xfermode_btn1:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                break;
            case R.id.xfermode_btn2:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                break;
            case R.id.xfermode_btn3:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                break;
            case R.id.xfermode_btn4:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                break;
            case R.id.xfermode_btn5:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                break;
            case R.id.xfermode_btn6:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                break;
            case R.id.xfermode_btn7:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                break;
            case R.id.xfermode_btn8:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
                break;
            case R.id.xfermode_btn9:
                xFermodeView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                break;
        }
    }
    //todo
    //        DARKEN,
    //        LIGHTEN,
    //        MULTIPLY,
    //        SCREEN,
    //        ADD,
    //        OVERLAY;
}
