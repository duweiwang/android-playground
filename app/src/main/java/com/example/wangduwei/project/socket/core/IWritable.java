package com.example.wangduwei.project.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:18
 **/
public interface IWritable {
    void write(ByteBuffer byteBuffer) throws IOException;

    void write(ByteBuffer byteBuffer, int var2, int var3) throws IOException;

    void flush() throws IOException;
}
