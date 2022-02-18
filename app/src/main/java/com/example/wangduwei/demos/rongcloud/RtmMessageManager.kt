package com.example.wangduwei.demos.rongcloud

import android.util.Log
import io.rong.imlib.RongIMClient

/**
 * @author 杜伟
 * @date 2022/2/18 10:19 AM
 *
 *
 *
 *
 */
class RtmMessageManager {

    companion object {
        private const val TAG = "RtmMessageManager"
    }


    private val mConnectCallback = object : RongIMClient.ConnectCallback() {
        override fun onSuccess(p0: String?) {
            Log.d(TAG, "connect--success $p0")
        }

        override fun onError(p0: RongIMClient.ConnectionErrorCode?) {
            Log.d(TAG, "connect--error $p0")
        }

        /**
         * 数据库回调.
         * @param code 数据库打开状态.
         * DATABASE_OPEN_SUCCESS 数据库打开成功;
         * DATABASE_OPEN_ERROR 数据库打开失败
         */
        override fun onDatabaseOpened(p0: RongIMClient.DatabaseOpenStatus?) {
            Log.d(TAG, "connect--onDatabaseOpened $p0")
        }
    }


    fun connect(token: String) {
        RongIMClient.connect(token, mConnectCallback)
    }


}