package com.example.wangduwei.demos.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/12
 */

public class SystemUtil {

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static boolean isNetworkOk(Context context){
        return true;
    }

}
