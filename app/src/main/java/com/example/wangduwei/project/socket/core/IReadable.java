package com.example.wangduwei.project.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:17
 **/
public interface IReadable {
    /**
     * @param byteBuffer
     * @return
     * @throws IOException
     */
    int read(ByteBuffer byteBuffer) throws IOException;

    /**
     * @param byteBuffer 读入的字节缓冲区
     * @param byteOffset
     * @param byteCount
     * @return
     * @throws IOException
     */
    int read(ByteBuffer byteBuffer, int byteOffset, int byteCount) throws IOException;

    /**
     * 读取一行
     *
     * @return
     * @throws IOException
     */
    String readLine() throws IOException;
}
