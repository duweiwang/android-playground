package com.example.wangduwei.demos.app

import android.content.Context

/**
 * @author 杜伟
 * @date 2020/8/19 下午7:29
 *
 */
interface IApplication {

    fun attachBaseContext(base: Context)

    fun onCreate()

    fun isMainProcess(): Boolean

}