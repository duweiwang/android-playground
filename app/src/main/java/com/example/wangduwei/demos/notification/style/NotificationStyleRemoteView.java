package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.wangduwei.demos.R;


/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleRemoteView extends NotificationStyle {

    public NotificationStyleRemoteView(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setContent(new RemoteViews(context.getPackageName(), R.layout.notification_remote_view));
        builder.setContentTitle("New Message" + number)
                .setGroup(NEW_SMS_RECEIVED_NOTIFICATION);
        int notifyId = notify(number, builder.build());
        return notifyId;
    }

    @Override
    protected int notify(int number, Notification notification) {
//        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        int notifyId = getNotificationId(number);
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }


}
