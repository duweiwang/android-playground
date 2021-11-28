package com.example.wangduwei.demos.lifecircle.demo2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.databinding.FragmentCoroutineBinding
import com.example.wangduwei.demos.main.BaseSupportFragment
import kotlinx.android.synthetic.main.fragment_coroutine.*

/**
 * 演示协程LiveData等的使用
 * @author : wangduwei
 * @since : 2020/5/11  15:51
 */
@PageInfo(description = "协程", navigationId = R.id.fragment_coroutine)
class CoroutineFragment : BaseSupportFragment() {

    private val binding by lazy {
        FragmentCoroutineBinding.inflate(layoutInflater)
    }

    private val viewModel: SingleRequestViewModel by viewModels();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutine_btn.setOnClickListener {
            viewModel.performRequest()
        }

        viewModel.mData().observe(this) {
            Log.d("wdw-coroutine", it)
            coroutine_text.text = it
        }
    }

}