package com.example.wangduwei.demos.app

import android.app.Application
import android.content.Context
import com.example.wangduwei.demos.router.RouterManager
import io.rong.imlib.RongIMClient

/**
 * @author 杜伟
 * @date 2021/11/23 2:08 PM
 *
 */
class MainApp(application: Application):AbsApp(application) {
    override fun isMainProcess(): Boolean = true


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        RouterManager.getInstance().init();
    }


    override fun onCreate() {
        super.onCreate()
        RongIMClient.init(application.applicationContext, "c9kqb3rdcq1nj",false);
    }
}