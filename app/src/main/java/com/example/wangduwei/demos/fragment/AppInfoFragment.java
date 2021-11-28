package com.example.wangduwei.demos.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.AppState;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/16
 */
@PageInfo(description = "App基础信息",navigationId = R.id.fragment_info_app_detail)
public class AppInfoFragment extends BaseSupportFragment {
    private TextView textView;
    StringBuilder sb = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(container.getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView = new TextView(container.getContext());
        textView.setTextSize(20);
        scrollView.addView(textView, lp);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText(sb.toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        AppState.INSTANCE.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        /**
         *            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) -
         * This method returns the standard, shared and recommended location for saving pictures and videos.
         * This directory is shared (public), so other applications can easily discover, read, change and delete files
         * saved in this location. If your application is uninstalled by the user, media files saved to this location
         * will not be removed. To avoid interfering with users existing pictures and videos, you should create a
         * sub-directory for your application's media files within this directory, as shown in the code sample below.
         * This method is available in Android 2.2 (API Level 8), for equivalent calls in earlier API versions, see
         * Saving Shared Files.
         *             Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) -
         *This method returns a standard location for saving pictures and videos which are associated with your application.
         *If your application is uninstalled, any files saved in this location are removed. Security is not enforced for files
         *in this location and other applications may read, change and delete them.
         */

        String appInfo = "";//getApplicationString();
        String dirInfo = getDirectoryString();
        String memInfo = getMemInfo();

        sb.append(appInfo + "--------" + dirInfo + memInfo );
    }



    /**
     * context对象的目录信息
     *
     * @return
     */
    private String getDirectoryString() {
        StringBuilder stringBuilder = new StringBuilder("Android 目录信息\n\n");
        Context context = AppState.INSTANCE.getContext();
        if (context.getCacheDir() != null) {
            stringBuilder.append("context.getCacheDir() = " +
                    context.getCacheDir().getAbsolutePath() + "\n\n");
        }
        if (context.getExternalCacheDir() != null) {
            stringBuilder.append("context.getExternalCacheDir() = " +
                    context.getExternalCacheDir().getAbsolutePath() + "\n\n");
        }
        if (context.getFilesDir() != null) {
            stringBuilder.append("context.getFilesDir() = " +
                    context.getFilesDir().getAbsolutePath() + "\n\n");
        }
        if (context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) != null) {
            stringBuilder.append("context.getExternalFilesDir(DIRECTORY_MUSIC) = " +
                    context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "\n\n");
        }
        if (context.getObbDir() != null) {
            stringBuilder.append("context.getObbDir() = " +
                    context.getObbDir().getAbsolutePath() + "\n\n");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (context.getDataDir() != null) {
                stringBuilder.append("context.getDataDir() = " +
                        context.getDataDir().getAbsolutePath());
            }
        }

        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    /**
     * Application对象提供的信息
     *
     * @return
     */
    private String getApplicationString() {
        StringBuilder stringBuilder = new StringBuilder();
        PackageManager packageManager = AppState.INSTANCE.getContext().getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(AppState.INSTANCE.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        stringBuilder.append("dataDir = " + applicationInfo.dataDir + "\n\n");
        try {
            stringBuilder.append("minSdkVersion = " +
                    applicationInfo.minSdkVersion != null ? applicationInfo.minSdkVersion : "" + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuilder.append("targetSdkVersion = " + applicationInfo.targetSdkVersion + "\n\n");
        stringBuilder.append("sourceDir = " + applicationInfo.sourceDir + "\n\n");
        stringBuilder.append("publicSourceDir = " + applicationInfo.publicSourceDir + "\n\n");

        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }


    private String getMemInfo() {
        StringBuilder builder = new StringBuilder("内存信息");
        ActivityManager am = (ActivityManager) AppState.INSTANCE.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{Process.myPid()});

        am.getMemoryInfo(mi);

        builder.append("系统空闲内存 = " + mi.availMem + "\n");
        builder.append("进程内存上限 = " + Runtime.getRuntime().maxMemory() / 1024 + "\n");
        builder.append("进程总内存 = " + memoryInfos[0].getTotalPss() + "\n");

        builder.append("判断鸿蒙 = " + (isHarmonyOs() ? "true" : "false"));

        return builder.toString();
    }

    public final boolean isHarmonyOs() {
        try {
            Class cls = Class.forName("ohos.utils.system.SystemCapability");
            return cls != null;
        } catch (Exception e) {
            return false;
        }
    }

    private double getProcessCpuUsage(String pid) {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    private String linuxCommand(String commond) throws IOException {
        java.lang.Process process = Runtime.getRuntime().exec(commond);

        OutputStream outputStream = process.getOutputStream();

        byte[] bytes = new byte[1024];
        outputStream.write(bytes);
        return bytes.toString();
    }

}
