package com.example.wangduwei.project.socket.core;

import java.nio.ByteBuffer;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:15
 **/
public interface ISocketCore {
    void connect();

    void close();

    boolean isConnected();

    boolean isClose();

    void send(ByteBuffer byteBuffer);

    void setListener(ISocketCoreListener iSocketCoreListener);
}
