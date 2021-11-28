package com.example.wangduwei.demos.main.appinfo.cpu

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView

import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.AbsBusinessDelegate
import com.example.wangduwei.demos.utils.CPUUtil

class CpuInfoDelegate : AbsBusinessDelegate() {

    private lateinit var mCpuText: TextView

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        mCpuText = view.findViewById(R.id.info_cpu)
        mCpuText.text = "  name : ${CPUUtil.getCpuName()} \n  " +
                "最大频率：${CPUUtil.getMaxCpuFreq()} \n  " +
                "最小频率：${CPUUtil.getMinCpuFreq()} \n  " +
                "当前频率：${CPUUtil.getCurCpuFreq()} \n  " +
                "核心数：${CPUUtil.getHeart()} \n  " +
                "温度：${CPUUtil.getCpuTemp()} \n  " +
                "Abi: ${CPUUtil.putCpuAbi()} \n  "
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
