package com.example.wangduwei.project.socket.core;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:15
 **/
public interface ISocketCoreListener {
    void onConnected();

    void onClose(int var1);

    void onError(Exception exception);

    void onSent();

    void onMessage(Object object);
}
