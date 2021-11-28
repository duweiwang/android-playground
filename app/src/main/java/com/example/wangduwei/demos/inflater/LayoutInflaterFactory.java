package com.example.wangduwei.demos.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/19
 */

public class LayoutInflaterFactory implements LayoutInflater.Factory2 {
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.d("wdw", "111: parent = " + parent + ",name = " + name);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.d("wdw", attrs.getAttributeName(i) + "=" + attrs.getAttributeValue(i));
        }
        return null;
    }

    /**
     * Factory2接口继承Factory接口，所以必须实现该方法，但是不执行该方法
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Log.d("wdw", "333");
        return null;
    }
}
