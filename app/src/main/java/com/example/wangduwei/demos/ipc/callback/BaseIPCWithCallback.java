package com.example.wangduwei.demos.ipc.callback;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;

/**
 * @desc: 带有回调的抽象业务类
 * @author: duwei
 * @date:2018/8/23
 */

public abstract class BaseIPCWithCallback<CALLBACK extends Binder, INTERFACE extends IInterface> implements ServiceConnection {

    private final CALLBACK callback;
    protected volatile INTERFACE mService;
    protected StateListener l;
    public void setListener(StateListener l){
        this.l = l;
    }

    protected BaseIPCWithCallback() {
        this.callback = createCallBack();
    }

    /**
     * 这两个是必须实现的接口
     */
    protected abstract void registeCallback(final CALLBACK callback, final INTERFACE service) throws RemoteException;

    protected abstract void unregisteCallback(final CALLBACK callback, final INTERFACE service) throws RemoteException;
//    protected abstract void sleepAndCallback() throws RemoteException;

    /**
     * 给回调变量赋值，初始化回调对象
     *
     * @return
     */
    abstract CALLBACK createCallBack();

    /**
     * 转换成通讯接口对象
     *
     * @return
     */
    abstract INTERFACE asInterface(IBinder remote);

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.mService = asInterface(service);
        l.onMessage("连接远程服务成功...");
        try {
            registeCallback(callback, mService);
            l.onMessage("注册回调成功...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        try {
            Log.d("ipc--","连接断开了");
            unregisteCallback(callback, mService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public interface StateListener{
        void onMessage(String msg);
    }
}
