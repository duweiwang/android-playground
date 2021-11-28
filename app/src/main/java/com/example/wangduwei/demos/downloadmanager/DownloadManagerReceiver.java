package com.example.wangduwei.demos.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/12/12
 */

public class DownloadManagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE://下载完成的广播
                long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d("wdw", "收到下载完成的广播，ID= " + completeId);
                break;
            case DownloadManager.ACTION_NOTIFICATION_CLICKED://点击下载中的任务的广播
                long clickedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d("wdw", "收到点击的广播，ID= " + clickedId);
                break;
            //启动Activity用，上面两个是广播用的
//            case DownloadManager.ACTION_VIEW_DOWNLOADS://启动一个activity显示所有的下载
//                break;
        }
    }
}
