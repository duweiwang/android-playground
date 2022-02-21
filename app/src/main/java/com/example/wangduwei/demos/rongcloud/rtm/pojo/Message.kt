package com.example.wangduwei.demos.rongcloud.rtm.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author 杜伟
 * @date 2022/2/19 3:54 PM
 *
 */
class Message<T>(
    @SerializedName("sid")
    private val id: String,
    @SerializedName("cid")
    private val channelId: String,
    @SerializedName("ts")
    private val timestamp: Long,
    @SerializedName("type")
    private val type: Int,
    @SerializedName("stype")
    private val subType: Int,
    @SerializedName("payload")
    private val payload: T
) : Serializable