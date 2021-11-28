package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationStyleInbox extends NotificationStyle {

    public NotificationStyleInbox(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        String[] events = new String[6];
        events[0] = "Hello my one world";
        events[1] = "Hello my two world";
        events[2] = "Hello my three world";
        events[3] = "Hello my four world";
        events[4] = "Hello my five world";
        events[5] = "Hello my six world";
        NotificationCompat.InboxStyle inboxStyle =new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Inbox tracker details:");
            for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        inboxStyle.setBigContentTitle("Thers are six messages");
        inboxStyle.setSummaryText("It's so easy,right?");
        builder.setContentTitle("2 new messages wtih abc" )
                .setContentText("subject")
                .setStyle(inboxStyle)
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
