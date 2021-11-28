package com.example.customview.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/3  14:20
 **/
public class DimensUtil {


    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


}
