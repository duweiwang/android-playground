package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.wangduwei.demos.notification.DeleteReceiver;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleNormal extends NotificationStyle {

    private static final String DELETE_ACTION = "NOTIFICATION_DELETE_ACTION";

    public NotificationStyleNormal(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        Intent deleteIntent = new Intent();
        deleteIntent.setClass(context, DeleteReceiver.class);
        deleteIntent.setAction(DELETE_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                new Random().nextInt(10000), deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setContentTitle("New Message" + number)
                .setGroup(NEW_SMS_RECEIVED_NOTIFICATION)
                .setDeleteIntent(pendingIntent)
                .setAutoCancel(true);//设置autoCancel=true后的自动删除消息，deleteIntent并不被启动！

        boolean isHide = true;
        if( isHide) {
            //Android 5.0之后支持在锁屏定义
            builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        } else {
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }

        Notification notification = builder.build();
//        notification.tickerText = "notification.tickerText";
        int notifyId = notify(number, notification);
        return notifyId;
    }

    @Override
    protected int notify(int number, Notification notification) {
        boolean isSameId = false;// TODO: 16-12-20 修改此值看效果(notifyId代表这唯一的一条通知)
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        if( isSameId) {
            notifyId = getNotificationId(number);
        }
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }


}
