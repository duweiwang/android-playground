package com.example.wangduwei.demos.plugin;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * 获取ActivityThread里面的H
 */
public class HCallback implements Handler.Callback {
    public static final int LAUNCH_ACTIVITY = 100;
    private Handler mHandler;

    public HCallback(Handler handler) {
        mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == LAUNCH_ACTIVITY) {
            Object r = message.obj;//ActivityClientRecord 对象
            try {
                Intent intent = (Intent) FieldUtil.getField(r.getClass(), r, "intent");

                Intent target = intent.getParcelableExtra(HookHelper.TARGET_INTENT);

                intent.setComponent(target.getComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(message);
        return true;
    }
}
