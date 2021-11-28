package com.example.wangduwei.demos.ipc.pool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/7/27
 */

public class BinderPoolManager {
    private static final String TAG = "BinderPoolManager";
    public static final int BINDER_NONE = -1;

    private static volatile BinderPoolManager sInstance;//确保并发取值正确性
    private final Context mContext;
    //控制多个线程时，某一线程中代码执行顺序；是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完后再执行
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private IBinderPool mBinderPool;

    public BinderPoolManager(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPoolManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPoolManager.class) {
                if (sInstance == null) {
                    sInstance = new BinderPoolManager(context);
                }
            }
        }
        return sInstance;
    }

    private void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IBinder.DeathRecipient mBinderDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "binderDied: ");
            mBinderPool.asBinder().unlinkToDeath(mBinderDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    public IBinder queryBinder(int bindCode) {
        IBinder binder = null;
        try {
            binder = mBinderPool.queryBinder(bindCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}