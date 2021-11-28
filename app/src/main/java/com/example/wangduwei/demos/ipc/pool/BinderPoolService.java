package com.example.wangduwei.demos.ipc.pool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.IBinderPool;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/7/27
 */

public class BinderPoolService extends Service {
    private static final String TAG = "BinderPoolService";
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private IBinder binderPool = new BinderPoolImpl();
    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return binderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder =null;
            switch (binderCode) {
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
            }
            return binder;
        }
    }
}
