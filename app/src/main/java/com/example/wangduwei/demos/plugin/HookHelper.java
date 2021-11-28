package com.example.wangduwei.demos.plugin;

import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";

    public static void hookAMS() throws Exception {
        Object defaultSingleton = null;
        if (Build.VERSION.SDK_INT >= 26) {//Android 8.0中获取IActivityManagerSingleton字段
            Class<?> activityManagerClazz = Class.forName("com.android.ActivityManager");

            defaultSingleton = FieldUtil.getField(activityManagerClazz, null, "IActivityManagerSingleton");
        } else {//其他版本获取gDefault字段
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");

            defaultSingleton = FieldUtil.getField(activityManagerNativeClazz, null, "gDefault");
        }


        Class<?> singletonClazz = Class.forName("android.util.Singleton");

        Field mInstanceField = FieldUtil.getField(singletonClazz, "mInstance");

        Object iActivityManager = mInstanceField.get(defaultSingleton);//获取到IActivityManager对象

        Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager");

        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iActivityManagerClazz},
                new IActivityManagerProxy(iActivityManager));

        mInstanceField.set(defaultSingleton, proxy);
    }

    /**
     * 替换ActivityThread的mH
     * @throws Exception
     */
    public static void hookHandler() throws Exception {
        Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread");

        Field mHField = FieldUtil.getField(activityThreadClazz, "mH");
        Handler mH = (Handler) mHField.get(currentActivityThread);

        FieldUtil.setField(Handler.class, mH, "mCallback", new HCallback(mH));
    }


}
