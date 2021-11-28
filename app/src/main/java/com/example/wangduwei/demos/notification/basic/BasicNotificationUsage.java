package com.example.wangduwei.demos.notification.basic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.notification.NotificationJumpActivity;
import com.example.wangduwei.demos.notification.style.NotificationUtils;

/**
 * @desc: 这里是基本使用，以及各种注释
 * <p>
 * {@link android.app.NotificationChannel} 这个是通知的渠道（类别），从安卓8.0开始加入的
 * 可以在通知的设置界面对每种类型的Channel进行单独的设置.可以向指定的Channel发送通知。
 * <p>
 * {@link TaskStackBuilder}用于实现跳转到指定界面后，能返回主界面，而不是返回Launcher。记得在Manifest声明parent！
 * <p>
 * 一些好文章
 * https://mp.weixin.qq.com/s/UsW76o2PkzO1UQUVj3T1yQ
 * @auther:duwei
 * @date:2018/9/17
 */

public class BasicNotificationUsage {

    private Context mContext;
    private NotificationManager mNotificationManager;


    long[] vibrate = new long[]{0, 500, 1000, 1500};

    public BasicNotificationUsage(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0--创建一个渠道
            (new NotificationUtils()).createDefaultChannel(mNotificationManager);
        }
        Intent intent = new Intent();
        intent.setClass(mContext, NotificationJumpActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addParentStack(NotificationJumpActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NotificationUtils.DEFAULT_CHANNEL_ID);
        /**三个必备元素**/
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Title");
        builder.setContentText("Content Text");
        /**可配置元素*/
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setTicker("Ticker Ticker Ticker Ticker");
        builder.setVibrate(vibrate);//自定义震动效果

        mNotificationManager.notify(123, builder.build());
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteDefaultChannel() {
        NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(NotificationUtils.DEFAULT_CHANNEL_ID);
        mNotificationManager.deleteNotificationChannel(NotificationUtils.DEFAULT_CHANNEL_ID);
    }

    public void openChannelSetting() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationUtils.DEFAULT_CHANNEL_ID);
        if (mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            mContext.startActivity(intent);
        }
    }

}
