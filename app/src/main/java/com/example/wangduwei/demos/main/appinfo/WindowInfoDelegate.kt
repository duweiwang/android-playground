package com.example.wangduwei.demos.main.appinfo

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.AbsBusinessDelegate
import kotlinx.coroutines.launch

class WindowInfoDelegate : AbsBusinessDelegate() {


    private lateinit var mTextView: TextView

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        mTextView = view?.findViewById<TextView>(R.id.info_window)!!



        view.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
//            observer(view)
        }

    }


    override fun onDestroy() {

    }


//    private suspend fun observer(view: View) {
//        val tracker = WindowInfoTracker.getOrCreate(view.context);
//        tracker.windowLayoutInfo(view.context as Activity)
//            .collect { layoutInfo ->
//                layoutInfo.displayFeatures.forEach {
//                    if (it is FoldingFeature) {
//                        // 这是折叠设备
//                        mTextView.text = "FoldingFeature"
//                    }
//                }
//
//            }
//    }
}