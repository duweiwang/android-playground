package com.example.wangduwei.demos.ipc.callback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * @desc: 声明在（:remote）进程
 * @auther:duwei
 * @date:2018/8/23
 */

public class RemoteService extends Service {
    private IPCRemoteHandler mHandle;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandle = new IPCRemoteHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mHandle.onBind(intent);
    }
}
