package com.example.wangduwei.demos.main

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.duwei.alive.SyncAccountHelper
import com.example.wangduwei.demos.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 入口Activity
 */
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("wdw-loader",classLoader.toString())

        setSupportActionBar(activity_main_toolbar)

        val host :NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = host.navController

        activity_main_bottom_navigation.setupWithNavController(navController)

        SyncAccountHelper.addAndOpenSyncAccount(application)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("wdw-event","Activity-dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("wdw-event","Activity-onTouchEvent")
        return super.onTouchEvent(event)
    }
}
