package com.example.wangduwei.demos.rongcloud.rtm.core

import android.content.Context
import com.example.wangduwei.demos.rongcloud.rtm.IRtmFacade
import com.example.wangduwei.demos.BuildConfig
import com.example.wangduwei.demos.rongcloud.rtm.core.proxy.ConnectionListenerProxy
import com.example.wangduwei.demos.rongcloud.rtm.core.proxy.MessageListenerProxy
import com.example.wangduwei.demos.rongcloud.rtm.core.proxy.OpListenerProxy
import io.rong.imlib.RongIMClient
import io.rong.imlib.chatroom.base.RongChatRoomClient

/**
 * @author 杜伟
 * @date 2022/2/19 2:27 PM
 *
 */
class RtmCore : IRtmFacade {
    companion object{
        const val TAG = "Rong-RtmCore"

        private const val TEST_APIKEY = "c9kqb3rdcq1nj"
        private const val RELEASE_APIKEY = ""
    }

    private var imClient: RongIMClient? = null

    private val connection = ConnectionListenerProxy()
    private val msgListener = MessageListenerProxy()
    private val opCallback = OpListenerProxy()


    override fun init(context: Context) {
        RongIMClient.init(
            context,
            if (BuildConfig.DEBUG) TEST_APIKEY else RELEASE_APIKEY,
            false
        )
    }

    override fun connect(token: String) {
        imClient = RongIMClient.connect(token, connection)
    }

    override fun disconnect() {
        imClient?.disconnect(false)
    }

    override fun subscribe() {
        RongIMClient.addOnReceiveMessageListener(msgListener)
    }

    override fun joinRoom(roomId: String, messageCount: Int) {
        RongChatRoomClient.getInstance().joinChatRoom(roomId, messageCount, opCallback)
    }

    override fun quiteRoom(roomId: String) {
        RongChatRoomClient.getInstance().quitChatRoom(roomId, opCallback)
    }


}