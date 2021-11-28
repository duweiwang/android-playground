package com.example.wangduwei.demos.ipc.messager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/3/27  12:01
 **/
public class RemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }
}
