package com.example.wangduwei.demos.fragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import com.example.wangduwei.demos.rongcloud.OneMessage
import com.google.gson.Gson
import io.rong.imlib.*
import kotlinx.android.synthetic.main.fragment_rongcloud.*
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import io.rong.imlib.chatroom.base.RongChatRoomClient
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.MessageContent
import io.rong.imlib.model.ReceivedProfile
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


/**
 * @author 杜伟
 * @date 2022/2/17 9:25 AM
 *
 */
@PageInfo(description = "融云消息", navigationId = R.id.fragment_rongcloud)
class RongCloudFragment : BaseSupportFragment() {
    companion object {
        private const val TAG = "Rong-Cloud"
    }

    val stringBuilder = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rongcloud, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connect.setOnClickListener {
            connectRongCloud()
        }
        receive.setOnClickListener {
            receiveMsg()
        }
        send.setOnClickListener {
            sendMsg()
        }
        register.setOnClickListener {
            registerCustomMsg()
        }
        join_room.setOnClickListener {
            joinChatRoom("123456789", 0)
        }
    }

    private fun registerCustomMsg() {
        val myMessages: ArrayList<Class<out MessageContent?>> = ArrayList()
        myMessages.add(OneMessage::class.java)
        RongIMClient.registerMessageType(myMessages)
    }

    private fun addText(text: String) {
        stringBuilder.append(text)
        stringBuilder.append("\n")
        text_info.text = stringBuilder.toString()
    }

    private fun connectRongCloud() {
        val token = "EW8hqp2TBSl7idB19tS5tCKw8m+UmKTe@11oa.cn.rongnav.com;11oa.cn.rongcfg.com"
        RongIMClient.connect(token, object : RongIMClient.ConnectCallback() {
            override fun onSuccess(p0: String?) {
                Log.d(TAG, "connect--success $p0")
                addText("链接融云成功")
            }

            override fun onError(p0: RongIMClient.ConnectionErrorCode?) {
                Log.d(TAG, "connect--error $p0")
                addText("链接融云失败 code = ${p0?.value}")
            }

            /**
             * 数据库回调.
             * @param code 数据库打开状态.
             * DATABASE_OPEN_SUCCESS 数据库打开成功; DATABASE_OPEN_ERROR 数据库打开失败
             */
            override fun onDatabaseOpened(p0: RongIMClient.DatabaseOpenStatus?) {
                Log.d(TAG, "connect--onDatabaseOpened $p0")
                addText("数据库连接 ${if (p0 == RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS) "成功" else "失败"}")
            }
        })
    }


    private fun receiveMsg() {
        RongIMClient.addOnReceiveMessageListener(object : OnReceiveMessageWrapperListener() {
            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */
//            override fun onReceived(
//                message: Message?,
//                left: Int,
//                hasPackage: Boolean,
//                offline: Boolean
//            ): Boolean {
//                return true
//            }

            override fun onReceivedMessage(message: Message?, profile: ReceivedProfile?) {
                Log.d(TAG,"收到消息 =  ${message?.content}")
                addText("收到消息 =  ${message?.content}")
            }
        })
    }


    private fun sendMsg() {
        val targetId = "123456789"

// 构建消息
        val message = Message.obtain(
            targetId,
            Conversation.ConversationType.CHATROOM,
            TextMessage.obtain("您好，这是从用户1发出的消息")
        )

        // 发送消息
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM,
            targetId,TextMessage.obtain("您好，这是从用户1发出的消息") ,null,null, object : IRongCallback.ISendMessageCallback {
                override fun onAttached(message: Message) {
                    Log.d(TAG, "发送消息 onAttached =  $message")
                    addText("发送消息 onAttached =  ${message.content}")
                }

                override fun onSuccess(message: Message) {
                    Log.d(TAG, "发送消息 onSuccess =  $message")
                    addText("发送消息 onSuccess =  ${message.content}")
                }

                override fun onError(message: Message, errorCode: RongIMClient.ErrorCode) {
                    Log.d(TAG, "发送消息 onError =  $message")
                    addText("发送消息 onError =  $message")
                }
            })
    }

    private fun joinChatRoom(chatroomId: String, defMessageCount: Int) {
        RongChatRoomClient.getInstance().joinChatRoom(chatroomId, defMessageCount,
            object : IRongCoreCallback.OperationCallback() {
                override fun onSuccess() {
                    addText("加入聊天室成功。。。")
                }

                override fun onError(coreErrorCode: IRongCoreEnum.CoreErrorCode?) {
                    addText("加入聊天室失败 ${coreErrorCode?.code}")
                }

            })

        RongChatRoomClient.setChatRoomAdvancedActionListener(object :
            RongChatRoomClient.ChatRoomAdvancedActionListener {
            /**
             * 正在加入聊天室时。
             */
            override fun onJoining(chatRoomId: String?) {
                addText("聊天室#onJoining roomid = $chatRoomId")
            }

            /**
             * 加入聊天室成功时。
             */
            override fun onJoined(chatRoomId: String?) {
                addText("聊天室#onJoined roomid = $chatRoomId")
            }

            /**
             * 触发时机：聊天室被重置时。加入聊天室成功，但是聊天室被重置，接收到此回调后，还会立刻收到onJoining 和 onJoined 回调。
             */
            override fun onReset(chatRoomId: String?) {
                addText("聊天室#onReset roomid = $chatRoomId")
            }

            /**
             * 退出聊天室时。
             */
            override fun onQuited(chatRoomId: String?) {
                addText("聊天室#onQuited roomid = $chatRoomId")
            }

            /**
             * 触发时机：聊天室被销毁时。 用户在线的时候房间被销毁才会收到此回调。
             */
            override fun onDestroyed(chatRoomId: String?,
                type: IRongCoreEnum.ChatRoomDestroyType?
            ) {
                addText("聊天室#onDestroyed roomid = $chatRoomId")
            }

            /**
             * 触发时机：聊天室操作异常时。
             */
            override fun onError(chatRoomId: String?, code: IRongCoreEnum.CoreErrorCode?) {
                addText("聊天室#onError roomid = $chatRoomId")
            }

        })
    }
}