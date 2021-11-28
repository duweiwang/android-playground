package com.example.wangduwei.demos.app

import android.app.Application

/**
 * @author 杜伟
 * @date 2021/11/23 2:19 PM
 *
 */
class DemoApp(application: Application):AbsApp(application) {
    override fun isMainProcess(): Boolean {
        return false
    }
}