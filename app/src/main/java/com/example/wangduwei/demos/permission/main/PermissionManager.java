package com.example.wangduwei.demos.permission.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/27
 */

public class PermissionManager {
    public static final String PERMISSION_READ_SDCARD = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static HashMap<String, ISingleRequestCallback> mSingleCallbackMap = new HashMap<>();
    private static HashMap<String, IMultiRequestCallback> mMultiCallback = new HashMap<>();

    /**
     * 判断是否有权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean havePermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getTargetSdkVersion(context) >= 23) {
                return ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                return PermissionChecker.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
            }
        }
        return true;
    }

    /**
     * 没有权限去请求
     *
     * @param permission
     * @param callback
     */
    public static void requestPermission(String permission, ISingleRequestCallback callback) {
        if (!mSingleCallbackMap.containsKey(permission)) {
            mSingleCallbackMap.put(permission, callback);
        }
        PermissionDialogHolderActivity.start(permission);
    }

    public static void requestPermissions(String name, String[] permissions, IMultiRequestCallback callback) {
        if (!mMultiCallback.containsKey(name)) {
            mMultiCallback.put(name, callback);
        }
        PermissionDialogHolderActivity.start(name, permissions);
    }


    public static void onRequestPermissionResult(@Nullable String name, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 1) {
            handleMulti(name, permissions, grantResults);
        } else {
            handleSingle(permissions, grantResults);
        }
    }


    private static int getTargetSdkVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static void handleSingle(@NonNull String[] permissions, @NonNull int[] grantResults) {
        ISingleRequestCallback requestCallback = null;
        if (mSingleCallbackMap.containsKey(permissions[0])) {
            requestCallback = mSingleCallbackMap.remove(permissions[0]);

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestCallback.onPermissionGranted();
            } else {
                requestCallback.onPermissionDenyed();
            }
        }
    }

    private static void handleMulti(@Nullable String name, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> mGrantedList = new ArrayList<>();
        List<String> mDenyedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                mDenyedList.add(permission);
            } else {
                mGrantedList.add(permission);
            }
        }
        if (mMultiCallback.containsKey(name)) {
            mMultiCallback.get(name).onCallback(mGrantedList, mDenyedList);
        }
    }

}
