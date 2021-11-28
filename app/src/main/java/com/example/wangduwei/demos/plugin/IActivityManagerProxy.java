package com.example.wangduwei.demos.plugin;

import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IActivityManagerProxy implements InvocationHandler {
    public static final String TAG = "";
    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("startActivity".equals(method.getName())) {//拦截startActivity方法
            Intent intent = null;
            int index = 0;
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) objects[index];
            Intent subIntent = new Intent();
            String pkgName = "";// TODO: 2019/3/31
            subIntent.setClassName(pkgName, pkgName + ".Activity");// TODO: 2019/3/31
            subIntent.putExtra(HookHelper.TARGET_INTENT, intent);
            objects[index] = subIntent;
        }


        return method.invoke(mActivityManager, objects);
    }
}
