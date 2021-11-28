package com.example.wangduwei.demos.service.communicate;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * @desc:主进程的服务
 * @auther:duwei
 * @date:2018/6/5
 */

public class ComService extends Service {

    private MyBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class MyBinder extends Binder {
        public void communicate() {
            Log.d("wdw", "connected..");
        }
    }
}
