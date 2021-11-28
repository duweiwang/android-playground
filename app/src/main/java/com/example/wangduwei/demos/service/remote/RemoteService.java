package com.example.wangduwei.demos.service.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.util.Log;

import com.example.wangduwei.demos.IMyAidlInterface;

/**
 * @desc: 子进程的服务
 * @auther:duwei
 * @date:2018/5/12
 */

public class RemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("demo","RemoteService::onBind");
        return new MyBinder();
    }

    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getName() throws RemoteException {
            return "test";
        }
    }

}
