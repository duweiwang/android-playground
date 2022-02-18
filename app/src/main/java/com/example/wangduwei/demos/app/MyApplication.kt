package com.example.wangduwei.demos.app

import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.example.customview.CustomViewAppState
import com.example.customview.MainMethodProvider
import com.example.wangduwei.demos.AppEnv
import com.example.wangduwei.demos.utils.AppUtils
import com.example.wangduwei.demos.utils.SystemUtil

/**
 *
 * @author : wangduwei
 * @since : 2020/4/7  13:11
 */
class MyApplication : Application() {
    private var app: IApplication? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        val classLoader = this.javaClass.classLoader
        Log.d("wdw-loader", "classLoader = $classLoader")

        val processName = SystemUtil.getProcessName(base, Process.myPid())

        if (processName == AppEnv.DEMO_PROCESS_NAME) {
            app = DemoApp(this)
        } else if (processName == AppEnv.MAIN_PROCESS_NAME) {
            app = MainApp(this)
        }
        app?.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        app?.onCreate()
        CustomViewAppState.init(provider)
    }

    var provider: MainMethodProvider = object : MainMethodProvider {
        override fun getScreenHeight(): Int {
            return AppUtils.getScreenHeight()
        }

        override fun getScreenWidth(): Int {
            return AppUtils.getScreenWidth()
        }

    }
}