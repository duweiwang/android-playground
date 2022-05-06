package com.example.wangduwei.demos.ipc.messager

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

/**
 *
 *
 *
 * @author : wangduwei
 * @since : 2020/3/27  12:01
 */
class RemoteService : Service() {

    private val messenge = Messenger(IncomingHandler())

    override fun onCreate() {
        super.onCreate()
        Log.d("wdw-messenger", "service onCreate")
    }

    override fun onBind(intent: Intent): IBinder? {
        return messenge.binder
    }

    class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val data = msg.data.getString("data")
            Log.d("wdw-messenger", "data = $data")
        }
    }
}