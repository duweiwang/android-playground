package com.example.wangduwei.demos.rongcloud.rtm.core.proxy

import android.util.Log
import com.example.wangduwei.demos.rongcloud.rtm.core.RtmCore
import io.rong.imlib.RongIMClient

/**
 * @author 杜伟
 * @date 2022/2/19 2:30 PM
 *
 */
class ConnectionListenerProxy : RongIMClient.ConnectCallback() {


    override fun onSuccess(t: String?) {
        Log.d(RtmCore.TAG, "connection-onSuccess $t")

    }

    override fun onError(e: RongIMClient.ConnectionErrorCode?) {
        Log.d(RtmCore.TAG, "connection-onError $e")

    }

    override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
        Log.d(RtmCore.TAG, "connection-onDatabaseOpened $code")

    }
}