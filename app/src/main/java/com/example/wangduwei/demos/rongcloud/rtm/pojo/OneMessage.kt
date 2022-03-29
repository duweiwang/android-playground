package com.example.wangduwei.demos.rongcloud.rtm.pojo

import android.util.Log
import io.rong.imlib.MessageTag
import io.rong.imlib.model.MessageContent
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

/**
 * @author 杜伟
 * @date 2022/2/18 10:28 AM
 *
 */
//自定义消息
@Parcelize
@MessageTag(
    value = "",//1. 消息唯一标识 2. 三端必须一致 3.不能以 ‘RC:’ 开头，以免和融云内置消息冲突
    flag = MessageTag.ISCOUNTED //消息属性，使用位掩码。
    //MessageTag.NONE	表示空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
    //MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1）。
    //MessageTag.ISPERSISTED	表示消息会存储到数据库。
)
class OneMessage() : MessageContent() {

    companion object {

        private const val TAG = ""

        fun obtain(): OneMessage = OneMessage()
    }

    constructor(data: ByteArray) : this() {
        if (data == null) {
            Log.e(TAG, "data is null ")
            return
        }

        var jsonStr: String? = null
        try {
            jsonStr = data.toString()//String(data, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.e(TAG, "UnsupportedEncodingException ", e)
        }

        if (jsonStr == null) {
            Log.e(TAG, "jsonStr is null ")
            return
        }

        try {
            val jsonObj = JSONObject(jsonStr)

            // 消息携带用户信息时, 自定义消息需添加下面代码
            if (jsonObj.has("user")) {
                userInfo = parseJsonToUserInfo(jsonObj.getJSONObject("user"))
            }

            // 用于群组聊天, 消息携带 @ 人信息时, 自定义消息需添加下面代码
            if (jsonObj.has("mentionedInfo")) {
                mentionedInfo = parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo"))
            }

            // ...
            // 自定义消息, 定义的字段
            // ...
        } catch (e: JSONException) {
            Log.e(TAG, "JSONException " + e.message)
        }
    }

    override fun encode(): ByteArray? {
        val jsonObj = JSONObject()
        try {
            // 消息携带用户信息时, 自定义消息需添加下面代码
            if (jsonUserInfo != null) {
                jsonObj.putOpt("user", jsonUserInfo)
            }

            // 用于群组聊天, 消息携带 @ 人信息时, 自定义消息需添加下面代码
            if (jsonMentionInfo != null) {
                jsonObj.putOpt("mentionedInfo", jsonMentionInfo)
            }

            // ...
            // 自定义消息, 定义的字段.
            // ...
        } catch (e: JSONException) {
            Log.e(TAG, "JSONException " + e.message)
        }
//        mutableListOf<>()

        try {
            return jsonObj.toString().toByteArray(Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            Log.e(TAG, "UnsupportedEncodingException ", e)
        }
        return null
    }

}