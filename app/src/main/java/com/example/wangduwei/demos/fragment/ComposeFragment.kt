package com.example.wangduwei.demos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

/**
 * @author 杜伟
 * @date 2022/4/19 5:00 PM
 *
 */
@PageInfo(description = "Compose", navigationId = R.id.fragment_compose)
class ComposeFragment : BaseSupportFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



}