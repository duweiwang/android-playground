package com.example.wangduwei.demos.main.appinfo.build

import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.TextView

import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.AbsBusinessDelegate
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 *
 */
class BuildInfoDelegate : AbsBusinessDelegate() {

    private var mTextView: TextView? = null

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        mTextView = view.findViewById(R.id.info_phone)
        mTextView!!.setText(getPhoneInfo())
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getPhoneInfo(): String {
        val sb = StringBuilder()
        sb.append("Build类中包含的数据 = \n")
        sb.append("\n\n Build.MANUFACTURER = " + Build.MANUFACTURER + "\n")//Xiaomi
        sb.append("Build.BOARD = " + Build.BOARD + "\n")//感觉没啥用
        sb.append("Build.HARDWARE = " + Build.HARDWARE + "\n")//感觉没啥用
        sb.append("Build.PRODUCT = " + Build.PRODUCT + "\n")//感觉没啥用

        sb.append("root文件夹下包含的文件 = \n")
        val files = Environment.getRootDirectory().listFiles()
        for (file in files!!) {
            sb.append("[" + file.name + "] , ")
        }
        sb.append("\n build.prop中的信息")
        val properties = Properties()
        try {
            properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val entries = properties.entries
        entries.map {
            sb.append(it.key.toString() + "=" + it.value + "\n")
        }
        return sb.toString()
    }
}
