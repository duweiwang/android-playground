package com.example.wangduwei.demos.ipc.messager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import kotlinx.android.synthetic.main.fragment_ipc_messenger.*

/**
 *
 *
 *
 * @author : wangduwei
 * @since : 2020/3/27  12:02
 *
 * todo 现在是单向通讯，需要实现双向通信
 */
class MessagerIPCFragment : BaseSupportFragment() {

    private var messenger: Messenger? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ipc_messenger, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messenger_bind_service.setOnClickListener {
            requireContext().bindService(
                Intent(requireContext(), RemoteService::class.java),
                object : ServiceConnection {
                    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                        Log.d("wdw-messenger", "onServiceConnected")
                        messenger = Messenger(p1)

                    }

                    override fun onServiceDisconnected(p0: ComponentName?) {
                        Log.d("wdw-messenger", "onServiceDisconnected")
                    }

                }, Context.BIND_AUTO_CREATE
            )
        }
        messenger_send.setOnClickListener {
            val msg = Message.obtain().apply {
                data = Bundle().apply {
                    putString("data", "hello")
                }
            }
            messenger?.send(msg)
        }
    }

}