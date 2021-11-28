package com.example.wangduwei.demos.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DeleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Delete Intent Received.");
    }
}
