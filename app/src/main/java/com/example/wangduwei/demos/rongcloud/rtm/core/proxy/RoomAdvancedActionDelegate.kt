package com.example.wangduwei.demos.rongcloud.rtm.core.proxy

import android.util.Log
import com.example.wangduwei.demos.rongcloud.rtm.core.RtmCore
import io.rong.imlib.IRongCoreEnum
import io.rong.imlib.chatroom.base.RongChatRoomClient

/**
 * @author 杜伟
 * @date 2022/2/18 9:10 PM
 *
 */
class RoomAdvancedActionDelegate : RongChatRoomClient.ChatRoomAdvancedActionListener {

    /**
     * 正在加入聊天室时。
     */
    override fun onJoining(chatRoomId: String?) {
        Log.d(RtmCore.TAG, "聊天室#onJoining roomid = $chatRoomId")
    }

    /**
     * 加入聊天室成功时。
     */
    override fun onJoined(chatRoomId: String?) {
        Log.d(RtmCore.TAG, "聊天室#onJoined roomid = $chatRoomId")
    }

    /**
     * 触发时机：聊天室被重置时。加入聊天室成功，但是聊天室被重置，接收到此回调后，还会立刻收到onJoining 和 onJoined 回调。
     */
    override fun onReset(chatRoomId: String?) {
        Log.d(RtmCore.TAG, "聊天室#onReset roomid = $chatRoomId")
    }

    /**
     * 退出聊天室时。
     */
    override fun onQuited(chatRoomId: String?) {
        Log.d(RtmCore.TAG, "聊天室#onQuited roomid = $chatRoomId")
    }

    /**
     * 触发时机：聊天室被销毁时。 用户在线的时候房间被销毁才会收到此回调。
     */
    override fun onDestroyed(
        chatRoomId: String?,
        type: IRongCoreEnum.ChatRoomDestroyType?
    ) {
        Log.d(RtmCore.TAG, "聊天室#onDestroyed roomid = $chatRoomId")
    }

    /**
     * 触发时机：聊天室操作异常时。
     */
    override fun onError(chatRoomId: String?, code: IRongCoreEnum.CoreErrorCode?) {
        Log.d(RtmCore.TAG, "聊天室#onError roomid = $chatRoomId")
    }

}