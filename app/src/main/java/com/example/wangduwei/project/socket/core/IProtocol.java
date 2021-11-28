package com.example.wangduwei.project.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:24
 **/
public interface IProtocol {
    Object readFrom(IReadable iReadable) throws IOException;

    void writeTo(IWritable iWritable, ByteBuffer byteBuffer) throws IOException;
}
