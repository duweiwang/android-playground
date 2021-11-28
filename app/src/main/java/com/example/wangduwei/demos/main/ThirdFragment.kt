package com.example.wangduwei.demos.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wangduwei.demos.R

class ThirdFragment : BaseSupportFragment() {

    companion object {
        fun newInstance(): ThirdFragment =
                ThirdFragment().apply {
                    val bundle = Bundle()
                    arguments = bundle
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_third, null)
    }
}