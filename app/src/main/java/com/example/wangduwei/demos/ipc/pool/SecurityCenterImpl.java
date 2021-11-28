package com.example.wangduwei.demos.ipc.pool;

import android.os.RemoteException;
import android.util.Log;

import com.example.wangduwei.demos.ISecurityCenter;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/7/27
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub {
    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        Log.d("wdw-binder","加密Thread = " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
