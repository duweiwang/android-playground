package com.example.wangduwei.demos.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs
import com.duwei.lib_compose.basic.AnimationScreen
import com.duwei.lib_compose.basic.ButtonScreen
import com.duwei.lib_compose.basic.GestureScreen
import com.duwei.lib_compose.basic.TextScreen
import com.example.wangduwei.demos.main.BaseSupportFragment

/**
 * @author 杜伟
 * @date 2022/4/22 5:13 PM
 *
 */
class ComposeDetailFragment : BaseSupportFragment() {

    private val args by navArgs<ComposeDetailFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(inflater.context).apply {
            setContent {
                when (args.type.name) {
                    "text" -> {
                        TextScreen()
                    }
                    "button" -> {
                        ButtonScreen()
                    }
                    "animation" -> {
                        AnimationScreen()
                    }
                    "gestures"->{
                        GestureScreen()
                    }
                }
            }
        }
    }

}