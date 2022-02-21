package com.example.wangduwei.demos.rongcloud.rtm

import android.content.Context
import com.example.wangduwei.demos.rongcloud.rtm.core.RtmCore

/**
 * @author 杜伟
 * @date 2022/2/19 2:17 PM
 *
 * Real-time-message Manager
 *
 */
object RtmManager : IRtmFacade {

    private val core: RtmCore = RtmCore()

    /**
     * 初始化sdk
     */
    override fun init(context: Context) {
        core.init(context)
    }

    /**
     * 建立连接
     */
    override fun connect(token: String) {
        core.connect(token)
    }

    /**
     * 断开连接
     */
    override fun disconnect() {
        core.disconnect()
    }

    /**
     * 订阅消息
     */
    override fun subscribe() {
        core.subscribe()
    }

    /**
     * 加入房间
     */
    override fun joinRoom(roomId: String, messageCount: Int) {
        core.joinRoom(roomId)
    }

    /**
     * 退出房间
     */
    override fun quiteRoom(roomId: String) {
        core.quiteRoom(roomId)
    }


}