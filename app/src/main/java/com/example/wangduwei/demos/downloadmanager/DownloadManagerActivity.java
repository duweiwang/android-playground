package com.example.wangduwei.demos.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @desc: {@link android.app.DownloadManager}使用示例
 * 1.可以监听下载进度（两种获取进度监听回调的方法，1.ContentObserver 2.开线程不断查询）
 * 2.获取下载信息
 * @auther:duwei
 * @date:2018/12/12
 */

public class DownloadManagerActivity extends Activity {
    private static final String TAG = "wdw-download";
    private static final String URL = "http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk";
    private static DownloadManager mDownloadManager;
    DownloadManager.Request request;
    private BroadcastReceiver mDownloadReceiver;
    private long mDownloadId;

    private static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg1 = msg.arg1;
            int arg2 = msg.arg2;
            Log.d("wdw", "arg1 = " + arg1 + ",arg2 = " + arg2);
        }
    };
    private DownloadContentObserver observer = new DownloadContentObserver();

    //三秒定时刷新一次
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    Runnable command = new Runnable() {
        @Override
        public void run() {
            updateView();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadReceiver = new DownloadManagerReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        this.registerReceiver(mDownloadReceiver, intentFilter);

        request = new DownloadManager.Request(Uri.parse(URL));

        //网络类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);

        request.setMimeType("application/vnd.android.package-archive");//apk

        //存储路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "wdw.apk");

        //设置Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//看内部方法注释
        request.setTitle("我是Title");//notification 的title
        request.setDescription("我是Desc");//notification desc

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            } else {
                mDownloadId = mDownloadManager.enqueue(request);
                scheduledExecutorService.scheduleAtFixedRate(command, 0, 3, TimeUnit.SECONDS);
            }
        } else {
            mDownloadId = mDownloadManager.enqueue(request);
            scheduledExecutorService.scheduleAtFixedRate(command, 0, 3, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mDownloadId = mDownloadManager.enqueue(request);
            scheduledExecutorService.scheduleAtFixedRate(command, 0, 3, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(CONTENT_URI, true, observer);
    }

    /**
     * 查询一些下载信息
     *
     * @param id
     */
    private void queryDownloadState(long id) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PENDING:
                    Log.d(TAG, "status = STATUS_PENDING");
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Log.d(TAG, "status = STATUS_PAUSED");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.d(TAG, "status = STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d(TAG, "status = STATUS_SUCCESSFUL");
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.d(TAG, "status = STATUS_FAILED");
                    break;
            }
            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
            long columId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
            Log.d(TAG, "address = " + address + "\n" +
                    "bytes_downloaded = " + bytes_downloaded + "\n" +
                    "bytes_total = " + bytes_total + "\n" +
                    "title = " + title + "\n" +
                    "desc = " + description + "\n" +
                    "columId = " + columId + "\n" +
                    "fileName = " + filename + "\n" +
                    "url = " + url);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadReceiver);
        getContentResolver().unregisterContentObserver(observer);
    }


    class DownloadContentObserver extends ContentObserver {
        public DownloadContentObserver() {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateView();
        }

    }

    public void updateView() {
        int[] bytesAndStatus = getBytesAndStatus(mDownloadId);
        int currentSize = bytesAndStatus[0];//当前大小
        int totalSize = bytesAndStatus[1];//总大小
        int status = bytesAndStatus[2];//下载状态
        if (status == DownloadManager.STATUS_RUNNING) {
            Message.obtain(handler, 0, currentSize, totalSize, status).sendToTarget();
        }
    }

    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

}
