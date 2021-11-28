package com.example.wangduwei.demos.notification.style;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.notification.DeleteReceiver;
import com.example.wangduwei.demos.notification.NotificationJumpActivity;
import com.example.wangduwei.demos.notification.basic.BasicNotificationUsage;

import java.util.Random;

/**
 * Created by zhouchaohong on 16-12-20.
 */

public abstract class NotificationStyle {

    private static final String DELETE_ACTION = "NOTIFICATION_DELETE_ACTION";
    public static final String NEW_SMS_RECEIVED_NOTIFICATION = "new_sms_received_notification";
    public static final int NOTIFICATION_ID = 123;

    protected NotificationManager mNotifyManager;

    public NotificationStyle(Context context) {
        mNotifyManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    protected int getNotificationId(int number) {
        return NOTIFICATION_ID*1000 + number;
    }

    public abstract int showNotification(Context context,
                                         int number,
                                         long time);

    protected abstract int notify(int number, Notification notification);

    public void cancel(int notifyId) {
        mNotifyManager.cancel(notifyId);
    }


    protected NotificationCompat.Builder getNotificationBuild(Context context, int number, long time) {

        int appColor = context.getResources().getColor(R.color.colorAccent);
        int ledColor = context.getResources().getColor(R.color.colorPrimary);
        Bitmap bigIcon = NotificationUtils.drawableToBitamp(context.getResources().getDrawable(R.drawable.guide_enjoy_haha));
        String uriStr = "android.resource://" + context.getPackageName() + "/"+ R.raw.pipaxing;
        Uri soundUri = Uri.parse(uriStr);

        Intent intent = new Intent(context, NotificationJumpActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("time",time);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationJumpActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = null;

        //控制不同的PendingIntent的标记
        boolean updateCurrent = true;//// TODO: 16-12-20 修改此值，看效果
        int pendingFlag =  PendingIntent.FLAG_CANCEL_CURRENT; //原先相同reqestCode的pendingIntent被取消，点击无效
        if( updateCurrent) {
            pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT;//原先相同reqeustCode的pendingIntent中的数据被新的替代
        }

        int requestCode = 0;
        boolean differentPendingRequestCode = true;// TODO: 16-12-20 修改此值，看效果
        if( differentPendingRequestCode ) {
            requestCode = new Random().nextInt(1000);
        }

        pendingIntent = stackBuilder.getPendingIntent(requestCode, pendingFlag);

        Intent deleteIntent = new Intent();
        deleteIntent.setClass(context, DeleteReceiver.class);
        deleteIntent.setAction(DELETE_ACTION);

        PendingIntent pendinDeleteIntent = PendingIntent.getBroadcast(context,
                new Random().nextInt(10000), deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NotificationUtils.DEFAULT_CHANNEL_ID);
        builder.setColor(appColor)
                .setContentTitle("setContentTitle" + number)
                .setContentText("setContentText " + number + " when"  + time )
                .setContentInfo("setContentInfo")
                .setTicker("Message ticker,ticker,ticker") //android 5.0被废弃
                .setNumber(number)
                .setWhen(time)
                .setGroup(NEW_SMS_RECEIVED_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_close_black_24dp)
                .setLargeIcon(bigIcon)
               /* .setDefaults(Notification.DEFAULT_LIGHTS
                        | Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE)*/
                .setVibrate(new long[] {200,300,100,300,200,300})
                .setSound(soundUri)
                .setLights(ledColor, 1000, 2000)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendinDeleteIntent)
//                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH) //android4.l开始
                .setOngoing(false)
//                .setSortKey(NEW_SMS_RECEIVED_NOTIFICATION)
                .setAutoCancel(true);
        return builder;
    }

    public void notifyGroupMessage(Context context) {
        //7.0归集通知栏目
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationCount = manager.getActiveNotifications().length;
            if( notificationCount > 1 ) {
                Notification summaryNotification = new NotificationCompat.Builder(context)
                        .setContentTitle( notificationCount + " contacts have new messages")
                        .setSmallIcon(R.drawable.goemoji_1f60a)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("abcd")
                                .addLine("efgh")
                                .setBigContentTitle("O the world is so beautiful,you know how can we help" +
                                        "you to be happiness you will discard you system to the workd,ok?" +
                                        "love will be love, and the biggest system. " +
                                        "yes!")
                                .setSummaryText(notificationCount + " contacts have new messages"))
                        .setGroup(NEW_SMS_RECEIVED_NOTIFICATION)
                        .setGroupSummary(true)
                        .build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                int notifyId = NOTIFICATION_ID;
                notificationManagerCompat.notify(notifyId,summaryNotification);
                getActiveNotificaitonCount(context, NEW_SMS_RECEIVED_NOTIFICATION,notifyId );
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    public static int getActiveNotificaitonCount(Context context, @NonNull String groupKey, int summaryId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            StatusBarNotification[] notifications = manager.getActiveNotifications();
            int count = 0;
            int length = notifications.length;
            String key;
            for (int i = 0; i < length; i++) {
                Notification notification = notifications[i].getNotification();
                key = notification.getGroup();
                int id = notifications[i].getId();
                if (id != summaryId && key != null && key.equals(groupKey)) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }
}
