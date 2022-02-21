package com.example.wangduwei.demos.rongcloud.rtm.pojo.payload

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author 杜伟
 * @date 2022/2/19 4:12 PM
 *
 */
class GiftMsg(
    @SerializedName("sender")
    private val sender: User,
    @SerializedName("receiver")
    private val receiver: User,
    @SerializedName("gift")
    private val gift: Gift
) : Serializable


class User(
    @SerializedName("name")
    private val name: String,
    @SerializedName("id")
    private val id: String
) : Serializable


class Gift(
    @SerializedName("id")
    private val id: String,
    @SerializedName("count")
    private val count: Int
) : Serializable