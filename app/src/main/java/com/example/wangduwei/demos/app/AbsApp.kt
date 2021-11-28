package com.example.wangduwei.demos.app

import android.app.Application
import android.content.Context
import com.example.wangduwei.demos.AppState

/**
 * @author 杜伟
 * @date 2020/8/19 下午7:38
 *
 * 所有进程都需要走的初始化逻辑
 *
 */
abstract class AbsApp (protected val application: Application): IApplication {

    lateinit var mContext: Context

    override fun attachBaseContext(base: Context) {
        mContext = base

        AppState.init(base);
    }


    override fun onCreate() {

    }


}