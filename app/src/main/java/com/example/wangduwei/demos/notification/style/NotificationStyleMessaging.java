package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationStyleMessaging extends NotificationStyle {

    public NotificationStyleMessaging(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setContentTitle("2 new messages wtih abc" )
                .setContentText("subject")
                .setStyle((new NotificationCompat.MessagingStyle("Replay"))
                        .addMessage("abc", 0, "zhouchaohong@gmail.com")
                        .addMessage("def", 1, "dengchangmin@gmail.com"))
                .build();

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
