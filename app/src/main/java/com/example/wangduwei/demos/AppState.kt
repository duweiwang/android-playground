package com.example.wangduwei.demos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/12
 */

@SuppressLint("StaticFieldLeak")
object AppState {

    var context: Context? = null
        private set

    fun init(context: Context) {
        this.context = context
    }
}
