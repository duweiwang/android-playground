package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;


import com.example.wangduwei.demos.R;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleProgress extends NotificationStyle {

    private int mMax = 0;
    private int mProgress = 0;
    private int mNumber = 0;

    public NotificationStyleProgress(Context context) {
        super(context);
    }


    @Override
    public int showNotification(Context context, final int number, long time) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentTitle("正在下载").setContentText("下载进度")
                .setSmallIcon(R.drawable.ic_close_black_24dp);

        final NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        final int notifyId = getNotificationId(number);
        new Thread(new Runnable() {

            @Override
            public void run() {
                int progress = 0;
                for (; progress <= 100; progress += 10) {
                    builder.setProgress(100, progress, false);
                    notificationManager.notify(notifyId, builder.build());

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                builder.setContentText("下载完成");
                builder.setProgress(0, 0, false);
                notificationManager.notify(notifyId, builder.build());

            }
        }).start();
        return notifyId;
    }


    @Override
    protected int notify(int number, Notification notification) {
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }




}
