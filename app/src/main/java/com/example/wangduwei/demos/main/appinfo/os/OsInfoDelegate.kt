package com.example.wangduwei.demos.main.appinfo.os

import android.system.Os
import android.view.View

import com.example.wangduwei.demos.main.AbsBusinessDelegate
import kotlinx.android.synthetic.main.fragment_myself.*
import kotlinx.android.synthetic.main.fragment_myself.view.*

class OsInfoDelegate : AbsBusinessDelegate() {


    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        //OS信息
        view.info_os.text = OsInfo()
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun OsInfo(): String {
        val sb = StringBuilder()

        var pid = Os.getppid()//parent process id
        var ppid = Os.getpid()//process id

        var uid = Os.getuid()//returns the real user ID of the calling process.
        var euid = Os.geteuid()//returns the effective user ID of the calling process.

        var gid = Os.getgid()//returns the real group ID of the calling process.
        var egid = Os.getegid()//returns the effective group ID of the calling process.

        sb.append("pid = $pid 、" +
                "ppid = $ppid 、" +
                "uid = $uid 、" +
                "euid = $euid 、" +
                "gid = $gid、" +
                "egid = $egid")
        return sb.toString()
    }

}
