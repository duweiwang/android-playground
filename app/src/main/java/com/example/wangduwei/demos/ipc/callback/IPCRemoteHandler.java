package com.example.wangduwei.demos.ipc.callback;

import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.ICallback;
import com.example.wangduwei.demos.IRemoteService;

/**
 * @desc: 子进程的业务实现处理器
 * @auther:duwei
 * @date:2018/8/23
 */

public class IPCRemoteHandler extends IRemoteService.Stub {
    private final RemoteCallbackList<ICallback> callbackList = new RemoteCallbackList<>();

    @Override
    public void registeCallback(ICallback callback) throws RemoteException {
        Log.d("ipc--", "server registeCallback");
        callbackList.register(callback);
    }

    @Override
    public void unregisteCallBack(ICallback callback) throws RemoteException {
        Log.d("ipc--", "server unregisteCallBack");
        callbackList.unregister(callback);
    }

    @Override
    public void sleepAndCallback() throws RemoteException {
        try {
            Log.d("ipc--", "睡眠中，Thread = " + Thread.currentThread().getName());
            Thread.sleep(5000);

            final int n = callbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++) {
                    Log.d("ipc--", "睡眠结束，开始回调，Thread = " + Thread.currentThread().getName());
                    callbackList.getBroadcastItem(i).callback(new IPCBean(Parcel.obtain()));
                }
            } catch (RemoteException e) {
                Log.d("ipc--", "sleepAndCallback ..");
            } finally {
                callbackList.finishBroadcast();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent intent) {
        return this;
    }
}
