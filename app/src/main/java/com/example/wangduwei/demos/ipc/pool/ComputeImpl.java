package com.example.wangduwei.demos.ipc.pool;

import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.ICompute;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/7/27
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        try {
            Log.d("wdw-binder","计算Thread = " + Thread.currentThread().getName());
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return a + b;
    }
}
