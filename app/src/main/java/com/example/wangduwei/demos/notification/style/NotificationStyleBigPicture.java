package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;

import com.example.wangduwei.demos.R;

import java.util.Random;

public class NotificationStyleBigPicture extends NotificationStyle {

    public NotificationStyleBigPicture(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        Bitmap largeIcon = NotificationUtils.drawableToBitamp(context.getResources().getDrawable(R.drawable.guide_enjoy_haha));
        Bitmap bigPicture = NotificationUtils.drawableToBitamp(context.getResources().getDrawable(R.drawable.goemoji_1f60a));
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigLargeIcon(largeIcon)
                .bigPicture(bigPicture)
                .setSummaryText("bigPicture")
                .setBigContentTitle("bigContentTitle" +
                        "this is a beatiful world,there are many many flowers and cloud."));
        int notifyId = notify(number, builder.build());
        return notifyId;
    }

    @Override
    protected int notify(int number, Notification notification) {
        int notifyId = getNotificationId(number) + new Random().nextInt(1000);
        mNotifyManager.notify(notifyId, notification);
/*        mNotifyManager.cancel(notifyId);
        mNotifyManager.cancelAll();*/
        return notifyId;
    }


}
