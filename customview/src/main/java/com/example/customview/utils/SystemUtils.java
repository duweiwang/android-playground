package com.example.customview.utils;

import android.content.Context;

import com.example.customview.CustomViewAppState;

public class SystemUtils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getDisplayWidth(Context context){
        return CustomViewAppState.mMethod.getScreenWidth();
    }

    public static int getDisplayHeight(Context context){
        return CustomViewAppState.mMethod.getScreenHeight();
    }
}