package com.example.wangduwei.demos.main.appinfo.display;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class DisplayInfoPresenter {

    public DisplayInfoPresenter(IDisplayInfoView view) {
        //non
    }

    public String getDisplayInfoString(Context context) {
        return getMobScreen(context);
    }


    static String getMobScreen(Context context) {
        StringBuilder screenBean = new StringBuilder();
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            screenBean.append("\t density = " + displayMetrics.density + "\n");
            screenBean.append("\t densityDpi = " + displayMetrics.densityDpi + "\n");
            screenBean.append("\t widthPixels = " + displayMetrics.widthPixels + "\n");
            screenBean.append("\t heightPixels = " + displayMetrics.heightPixels + "\n");
            int screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            int screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            int screenChange = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            screenBean.append("\t automatic brightness = " + (screenMode == 1) + "\n");
            screenBean.append("\t 背景光 = " + screenBrightness + "\n");
            screenBean.append("\t 自动旋屏 = " + (screenChange == 1) + "\n");
            screenBean.append("\t checkHasNavigationBar = " + checkHasNavigationBar(context) + "\n");
            screenBean.append("\t checkHideStatusBar = " + checkHideStatusBar(context) + "\n");
            screenBean.append("\t StatusBarHeight =  " + getStatusBarHeight(context) + "\n");
            screenBean.append("\t NavigationBarHeight = " + getNavigationBarHeight(context) + "\n");
        } catch (Exception e) {
            Toast.makeText(context, "屏幕信息出错", Toast.LENGTH_SHORT).show();
        }
        return screenBean.toString();
    }


    private static boolean checkHideStatusBar(Context context) {
        return checkFullScreenByTheme(context) || checkFullScreenByCode(context) || checkFullScreenByCode2(context);
    }

    private static boolean checkFullScreenByTheme(Context context) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedValue typedValue = new TypedValue();
            boolean result = theme.resolveAttribute(android.R.attr.windowFullscreen, typedValue, false);
            if (result) {
                typedValue.coerceToString();
                if (typedValue.type == TypedValue.TYPE_INT_BOOLEAN) {
                    return typedValue.data != 0;
                }
            }
        }
        return false;
    }

    private static boolean checkFullScreenByCode(Context context) {
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            if (window != null) {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    return (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN)
                            == View.SYSTEM_UI_FLAG_FULLSCREEN;
                }
            }
        }
        return false;
    }

    private static boolean checkFullScreenByCode2(Context context) {
        if (context instanceof Activity) {
            return (((Activity) context).getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    == WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        return false;
    }

    private static boolean checkHasNavigationBar(Context context) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display d = windowManager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                d.getRealMetrics(realDisplayMetrics);
            }

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        }
        return false;
    }


    private static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
