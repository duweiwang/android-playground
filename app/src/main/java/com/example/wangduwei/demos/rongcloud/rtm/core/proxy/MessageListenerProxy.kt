package com.example.wangduwei.demos.rongcloud.rtm.core.proxy

import android.util.Log
import com.example.wangduwei.demos.rongcloud.rtm.core.RtmCore
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.Message
import io.rong.imlib.model.ReceivedProfile

/**
 * @author 杜伟
 * @date 2022/2/19 2:32 PM
 *
 *
 * 融云-收到消息回调
 *
 */
class MessageListenerProxy : OnReceiveMessageWrapperListener() {


    override fun onReceivedMessage(message: Message?, profile: ReceivedProfile?) {
        Log.d(RtmCore.TAG, "onReceivedMessage ${message?.content}")



    }


}