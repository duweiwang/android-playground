package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import com.example.wangduwei.demos.R;

import java.util.Random;

/**
 * 这里的代码实际无效果，原因需要进一步研究
 */

public class NotificationStyleBigHeadupRemoteView extends NotificationStyle {

    public NotificationStyleBigHeadupRemoteView(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        //Android7.0才也无效？ 这份代码没有任何效果
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setCustomHeadsUpContentView(new RemoteViews(context.getPackageName(), R.layout.notification_remote_view));
        builder.setContentTitle("New Message" + number)
                .setGroup(NEW_SMS_RECEIVED_NOTIFICATION);
        int notifyId = notify(number, builder.build());
        return notifyId;
    }

    @Override
    protected int notify(int number, Notification notification) {
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }


}
