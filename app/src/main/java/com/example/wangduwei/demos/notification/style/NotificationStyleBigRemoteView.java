package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import com.example.wangduwei.demos.R;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleBigRemoteView extends NotificationStyle {

    public NotificationStyleBigRemoteView(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        //Android7.0才有效
        //下边这个两个是一样的效果，缩略时显示文字，否则显示该布局
        builder.setCustomBigContentView(new RemoteViews(context.getPackageName(), R.layout.notification_remote_view));
        //builder.setCustomContentView(new RemoteViews(context.getPackageName(), R.layout.notification_remote_view));

        //Android7.0以下版本，只显示文字，没有显示这个布局

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
