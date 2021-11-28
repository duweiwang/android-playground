package com.example.wangduwei.demos.inflater;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc: 通过给系统LayoutInflater设置Factory，可以完成：
 * 1.优化布局加载速度，如果是自定义View，不走反射流程，直接拦截走我们自己的流程返回View对象
 * 2.动态替换View：如将所有指定类型的系统View换成我们自己想要的View
 * <p>
 * https://blog.csdn.net/u013085697/article/details/53898879
 * </p>
 * @auther:duwei
 * @date:2018/9/19
 */

public class InflaterFactoryDemo extends BaseSupportFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflaterFactory());
        super.onCreate(savedInstanceState);
    }
}
