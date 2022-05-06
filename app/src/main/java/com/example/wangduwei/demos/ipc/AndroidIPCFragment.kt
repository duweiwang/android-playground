package com.example.wangduwei.demos.ipc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

/**
 * @author 杜伟
 * @date 2022/5/6 1:48 PM
 *
 */
@PageInfo(
    description = "跨进程通讯。AIDL、Messenger",
    navigationId = R.id.fragment_android_ipc,
    title = "Android-IPC",
    preview = -1
)
class AndroidIPCFragment: BaseSupportFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}