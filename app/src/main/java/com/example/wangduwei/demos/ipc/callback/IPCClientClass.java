package com.example.wangduwei.demos.ipc.callback;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.ICallback;
import com.example.wangduwei.demos.IRemoteService;

/**
 * @desc: 带有回调的业务实现类
 * @auther:duwei
 * @date:2018/8/23
 */
public class IPCClientClass extends BaseIPCWithCallback<IPCClientClass.RemoteCallback, IRemoteService> {

    @Override
    protected void registeCallback(RemoteCallback remoteCallback, IRemoteService service)
            throws RemoteException {
        service.registeCallback(remoteCallback);
    }

    @Override
    protected void unregisteCallback(RemoteCallback remoteCallback, IRemoteService service)
            throws RemoteException {
        service.unregisteCallBack(remoteCallback);
    }

//    @Override
//    protected void sleepAndCallback() throws RemoteException {
//        mService.sleepAndCallback();
//    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        try {
            l.onMessage("开始远程睡眠...");
            mService.sleepAndCallback();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    RemoteCallback createCallBack() {
        return new RemoteCallback();
    }

    @Override
    IRemoteService asInterface(IBinder remote) {
        return IRemoteService.Stub.asInterface(remote);
    }


    protected static class RemoteCallback extends ICallback.Stub {
        @Override
        public void callback(IPCBean bean) throws RemoteException {
            Log.d("ipc--", "收到回调了...Thread = " + Thread.currentThread().getName());
        }
    }
}
