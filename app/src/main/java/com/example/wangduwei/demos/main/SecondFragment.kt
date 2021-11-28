package com.example.wangduwei.demos.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.wangduwei.demos.R

class SecondFragment : BaseSupportFragment() {
    companion object {
        fun newInstance(): SecondFragment =
                SecondFragment().apply {
                    val bundle = Bundle()
                    arguments = bundle
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.animation_test).setOnClickListener(this)
        view.findViewById<TextView>(R.id.interpolator_test).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view!!.id) {
            R.id.animation_test -> {
                findNavController().navigate(R.id.fragment_animation)
            }
            R.id.interpolator_test -> {
                findNavController().navigate(R.id.fragment_interpolator)
            }
        }
    }
}
