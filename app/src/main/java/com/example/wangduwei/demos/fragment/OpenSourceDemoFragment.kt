package com.example.wangduwei.demos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import com.example.wangduwei.demos.opensource.PopupMenuCascadeDelegate
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

/**
 * @author 杜伟
 * @date 2023/11/8 22:08
 *
 */
@PageInfo(description = "开源项目集合", navigationId = R.id.fragment_opensource_demo)
class OpenSourceDemoFragment : BaseSupportFragment() {

    private lateinit var displayDelegate: PopupMenuCascadeDelegate

    private var clickCount = 0;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_opensource_demo, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayDelegate = PopupMenuCascadeDelegate()
        displayDelegate.onViewCreated(view)


        view.findViewById<Button>(R.id.motion_toast).setOnClickListener {
            if (clickCount > 3) {
                clickCount = 0
            }

            handleClick()

            clickCount++
        }
    }

    private fun handleClick() {
        when (clickCount) {
            0 -> {
                MotionToast.createToast(
                    requireActivity(),
                    "Hurray success 😍",
                    "Upload Completed successfully!",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                )
            }
            1 -> {
                MotionToast.createToast(
                    requireActivity(),
                    "Failed ☹️",
                    "Profile Update Failed!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                )
            }
            2 -> {
                MotionToast.createToast(
                    requireActivity(),
                    "Please fill all the details!",
                    "message",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                )
            }
            3 -> {
                MotionToast.darkColorToast(
                    requireActivity(),
                    "Delete all history!",
                    "message",
                    MotionToastStyle.DELETE,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                )
            }
        }
    }

}