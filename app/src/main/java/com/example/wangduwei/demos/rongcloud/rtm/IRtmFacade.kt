package com.example.wangduwei.demos.rongcloud.rtm

import android.content.Context

/**
 * @author 杜伟
 * @date 2022/2/19 2:45 PM
 *
 */
interface IRtmFacade {

    fun init(context: Context)

    /**
     * 建立连接
     */
    fun connect(token: String)

    /**
     * 断开连接
     */
    fun disconnect()

    /**
     * 订阅消息
     */
    fun subscribe()

    /**
     * 加入房间
     */
    fun joinRoom(roomId: String, messageCount: Int = 20)

    /**
     * 退出房间
     */
    fun quiteRoom(roomId: String)
}