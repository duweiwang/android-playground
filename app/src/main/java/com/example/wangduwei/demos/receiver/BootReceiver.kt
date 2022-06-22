package com.example.wangduwei.demos.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.wangduwei.demos.service.ForegroundService

/**
 * @author 杜伟
 * @date 2022/6/22 3:16 PM
 *
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        Log.d("wdw-demo", "receiver action=${p1?.action}")

        val intent = Intent(context, ForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}