package com.duwei.alive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 后台执行任务的receiver
 */
class ExecutionReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "ExecutionReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.w(TAG, "onReceive --> action = ${intent?.action}")
    }
}