package com.example.wangduwei.demos.notification.style;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class NotificationUtils {
    public static final String DEFAULT_CHANNEL_ID = "1";///必須包内唯一
    public static final String DEFAULT_CHANNEL_NAME = "default";//用户在设置界面看见的名字

    public static Bitmap drawableToBitamp(Drawable drawable) {
        try {
            Bitmap bitmap;
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            System.out.println("Drawable转Bitmap");
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(w, h, config);
            //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        } catch (Throwable e) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createDefaultChannel(NotificationManager notificationManager) {
        NotificationChannel notificationChannel = new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);////是否在桌面icon右上角展示小红点
        notificationChannel.setLightColor(Color.GREEN);////小红点颜色
        notificationChannel.setShowBadge(true);////是否在久按桌面图标时显示此渠道的通知
        notificationChannel.setDescription("this is default channel");//描述信息
        notificationManager.createNotificationChannel(notificationChannel);
    }

}
