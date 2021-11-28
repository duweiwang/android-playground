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

public class NotificationStyleFullScreen extends NotificationStyle {

    public NotificationStyleFullScreen(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setContentTitle("setContentTitle" )
                .setContentText("setContentText")
                .setSmallIcon(R.drawable.ic_close_black_24dp)
                .setLargeIcon(NotificationUtils.drawableToBitamp(context.getResources().getDrawable(R.drawable.guide_enjoy_haha)))
                .setStyle((new NotificationCompat.MessagingStyle("Replay"))
                        .addMessage("abc", 0, "379906411@qq.com")
                        .addMessage("def", 1, "123456789@qq.com"))
                .setFullScreenIntent(getPendingIntent(context,number,time), true)
                .build();
        int notifyId = notify(number, builder.build());
        return notifyId;
    }

    private PendingIntent getPendingIntent(Context context, int number, long time) {
        Intent intent = new Intent(context, NotificationJumpActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("time",time);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationJumpActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(new Random().nextInt(1000), PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    protected int notify(int number, Notification notification) {
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        mNotifyManager.notify(notifyId, notification);
        return notifyId;
    }


}
