package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.notification.NotificationJumpActivity;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleMessagingAction extends NotificationStyle {

    public NotificationStyleMessagingAction(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setContentTitle("2 new messages wtih abc" )
                .setContentText("subject")
                .setStyle((new NotificationCompat.MessagingStyle("Replay"))
                        .addMessage("abc", 0, "zhouchaohong@gmail.com")
                        .addMessage("def", 1, "dengchangmin@gmail.com"));
        addActionButton(context,builder);
        int notifyId = notify(number, builder.build());
        return notifyId;
    }

    private NotificationCompat.Builder addActionButton(Context context,
                                                       NotificationCompat.Builder builder) {
        //Andorid 7.0引入的按钮动作
        PendingIntent pendingIntent = getActionPendingIntent(context);
        NotificationCompat.Action replyAction = new NotificationCompat.Action(
                R.drawable.goemoji_1f47f,"回复",pendingIntent
        );
        NotificationCompat.Action cancelAction = new NotificationCompat.Action(
                R.drawable.goemoji_1f60a,"取消",pendingIntent
        );
        builder.addAction(replyAction)
                .addAction(cancelAction);
        return builder;
    }

    private PendingIntent getActionPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationJumpActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationJumpActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(11111, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
    @Override
    protected int notify(int number, Notification notification) {
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }


}
