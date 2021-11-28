package com.example.wangduwei.demos.notification.style;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public class NotificationStyleBigText extends NotificationStyle {

    public NotificationStyleBigText(Context context) {
        super(context);
    }

    @Override
    public int showNotification(Context context, int number, long time) {
        NotificationCompat.Builder builder = getNotificationBuild(context,number,time);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("long long long ago, this is a canroso, the beautiful" +
                        "girl is printss, ksfjskfjd" +
                        "sfskjsfkfsjalll ,,sdkjksl,,fskdjz,fksj" +
                        "jksfdjsk,lsfkds"));
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
