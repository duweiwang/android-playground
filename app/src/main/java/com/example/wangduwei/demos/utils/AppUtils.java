package com.example.wangduwei.demos.utils;

import android.content.Context;
import android.view.WindowManager;

import com.example.wangduwei.demos.AppState;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/13
 */
public class AppUtils {


    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) AppState.INSTANCE.getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) AppState.INSTANCE.getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
