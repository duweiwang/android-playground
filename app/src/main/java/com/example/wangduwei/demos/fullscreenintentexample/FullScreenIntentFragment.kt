package com.example.wangduwei.demos.fullscreenintentexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

/**
 * @author 杜伟
 * @date 2024/4/22 13:38
 *
 */
@PageInfo(
    description = "FullScreenIntent",
    navigationId = R.id.fragment_fullscreenintent,
    title = "FullScreenIntent",
    preview = R.drawable.full_screen_intent
)
class FullScreenIntentFragment : BaseSupportFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fullscreen_test, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.showFullScreenIntentButton).setOnClickListener {
            requireContext().showNotificationWithFullScreenIntent()
        }

        view.findViewById<Button>(R.id.showFullScreenIntentWithDelayButton).setOnClickListener {
            requireContext().scheduleNotification(false)
        }

        view.findViewById<Button>(R.id.showFullScreenIntentLockScreenWithDelayButton)
            .setOnClickListener {
                requireContext().scheduleNotification(true)
            }
    }

}