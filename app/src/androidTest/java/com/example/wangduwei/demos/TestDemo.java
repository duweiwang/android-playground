package com.example.wangduwei.demos;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author : wangduwei
 * @date : 2020/8/23
 * @description :
 */
@RunWith(AndroidJUnit4.class)
class TestDemo {

    private static final String mPackageName = "com.example.wangduwei.demos";

    private Instrumentation instrumentation;
    private UiDevice uiDevice;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }


    @Test
    public void startAppTest() {
        try {
            if (!uiDevice.isScreenOn()) {
                uiDevice.wakeUp();//唤醒屏幕
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //mDevice.pressHome();  //按home键

        startAPP(mPackageName);  //启动app
        uiDevice.waitForWindowUpdate(mPackageName, 5 * 2000);//等待app
        closeAPP(uiDevice, mPackageName);//关闭app
    }

    private void startAPP(String sPackageName) {
        Context mContext = androidx.test.InstrumentationRegistry.getContext();
        Context applicationContext = ApplicationProvider.getApplicationContext();
//        Resources resourcesForApplication = PackageManager.getResourcesForApplication(mPackageName);

        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(sPackageName);  //通过Intent启动app
        mContext.startActivity(myIntent);
    }

    private void closeAPP(UiDevice uiDevice, String sPackageName) {
        Log.i("wdw-test", "closeAPP: ");
        try {
            uiDevice.executeShellCommand("am force-stop " + sPackageName);//通过命令行关闭app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAPP(UiDevice uiDevice, String sPackageName, String sLaunchActivity) {
        try {
            uiDevice.executeShellCommand("am start -n " + sPackageName + "/" + sLaunchActivity);//通过命令行启动app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
