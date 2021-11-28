package com.example.wangduwei.project.socket.core;

import androidx.core.util.Preconditions;

import com.example.wangduwei.project.socket.LogWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:14
 **/
public class IOSocketCore implements ISocketCore, Runnable {
    private ISocketCoreListener mListener;
    private IProtocol mProtocol;
    private String mDstName;
    private int mDstPort;
    private int mTimeout;
    private int mState = 0;
    private Socket mSocket;
    private IOSocketCore.Reader mReader;
    private IOSocketCore.Writer mWriter;
    private BlockingQueue<ByteBuffer> mSendQueue;
    private Thread mReceiverThread;
    private Thread mSenderThread;

    public IOSocketCore(String dstName, int dstPort, int timeout, IProtocol protocol, ISocketCoreListener listener) {
        Preconditions.checkNotNull(protocol, "protocol is not null");
        this.mDstName = dstName;
        this.mDstPort = dstPort;
        this.mTimeout = timeout;
        this.mProtocol = protocol;
        this.mSendQueue = new LinkedBlockingQueue();
        this.mListener = listener;
    }

    @Override
    public void connect() {
        if (this.mReceiverThread != null) {
            throw new IllegalStateException("SocketCore is Running");
        } else {
            this.mReceiverThread = new Thread(this, "SocketCore ReceiverThread");
            this.mReceiverThread.start();
        }
    }

    @Override
    public void close() {
        if (!this.isClose()) {
            this.attemptClose(1);
        }
    }

    @Override
    public boolean isConnected() {
        return this.mState == 1;
    }

    @Override
    public boolean isClose() {
        return this.mState == 2;
    }

    @Override
    public void send(ByteBuffer byteBuffer) {
        this.mSendQueue.add(byteBuffer);
    }

    @Override
    public void setListener(ISocketCoreListener listener) {
        this.mListener = listener;
    }

    private void setState(int mState) {
        this.mState = mState;
    }

    @Override
    public void run() {
        this.attemptConnect();
        if (this.isConnected()) {
            this.mSenderThread = new IOSocketCore.SenderThread();
            this.mSenderThread.start();
            this.attemptReceive();
        }

    }

    private void attemptConnect() {
        if (!this.isClose()) {
            try {
                this.mSocket = new Socket();
                this.mSocket.connect(new InetSocketAddress(this.mDstName, this.mDstPort), this.mTimeout);
                this.mReader = new IOSocketCore.Reader(this.mSocket.getInputStream());
                this.mWriter = new IOSocketCore.Writer(this.mSocket.getOutputStream());
                this.setState(1);
                this.onConnected();
            } catch (Exception e) {
                this.handleException(e, -1);
            }
        }
    }

    private void attemptReceive() {
        while (true) {
            try {
                if (!this.isClose()) {
                    this.onMessage(this.mProtocol.readFrom(this.mReader));
                    continue;
                }
            } catch (Exception var2) {
                LogWrapper.d("NewSocketTest", "==ReceiverThread Exception:" + var2);
                this.handleException(var2, 2);
            }

            return;
        }
    }

    private void handleException(Exception e, int closeCode) {
        LogWrapper.d("NewSocketTest", "==handleException:" + e);
        this.onError(e);
        this.attemptClose(closeCode);
    }

    private void attemptClose(int closeCode) {
        if (!this.isClose()) {
            LogWrapper.d("NewSocketTest", "==attemptClose==");
            if (this.mSenderThread != null) {
                this.mSenderThread.interrupt();
            }

            this.mSendQueue.clear();
            this.setState(2);
            this.onClose(closeCode);
        }
    }

    private void closeSocket() {
        LogWrapper.d("NewSocketTest", "==closeSocket==");

        try {
            if (this.mSocket != null) {
                this.mSocket.close();
            }
        } catch (Exception var2) {
            LogWrapper.d("NewSocketTest", "==closeSocket exception:" + var2);
            this.onError(var2);
        }

    }

    private void onConnected() {
        if (this.mListener != null) {
            this.mListener.onConnected();
        }

    }

    private void onClose(int closeCode) {
        if (this.mListener != null) {
            this.mListener.onClose(closeCode);
        }

    }

    private void onError(Exception e) {
        if (!this.isClose()) {
            if (this.mListener != null) {
                this.mListener.onError(e);
            }

        }
    }

    private void onMessage(Object receive) {
        if (this.mListener != null) {
            this.mListener.onMessage(receive);
        }
    }

    private void onSent() {
        if (this.mListener != null) {
            this.mListener.onSent();
        }
    }

    private static class Writer implements IWritable {
        private OutputStream mOutputStream;

        private Writer(OutputStream outputStream) {
            this.mOutputStream = outputStream;
        }

        @Override
        public void write(ByteBuffer buffer) throws IOException {
            this.mOutputStream.write(buffer.array());
        }

        @Override
        public void write(ByteBuffer buffer, int offset, int count) throws IOException {
            this.mOutputStream.write(buffer.array(), offset, count);
        }

        public void flush() throws IOException {
            this.mOutputStream.flush();
        }
    }

    private static class Reader implements IReadable {
        private InputStream mInputStream;
        private BufferedReader mBufferReader;

        private Reader(InputStream inputStream) {
            this.mInputStream = inputStream;
        }

        @Override
        public int read(ByteBuffer buffer) throws IOException {
            return this.mInputStream.read(buffer.array());
        }

        @Override
        public int read(ByteBuffer buffer, int byteOffset, int byteCount) throws IOException {
            return this.mInputStream.read(buffer.array(), byteOffset, byteCount);
        }

        public String readLine() throws IOException {
            if (this.mBufferReader == null) {
                this.mBufferReader = new BufferedReader(new InputStreamReader(this.mInputStream));
            }

            return this.mBufferReader.readLine();
        }
    }

    private class SenderThread extends Thread {
        private SenderThread() {
        }

        @Override
        public void run() {
            Thread.currentThread().setName("SocketCore SenderThread");

            try {
                while (!Thread.interrupted()) {
                    ByteBuffer requestBuffer = mSendQueue.take();
                    mProtocol.writeTo(mWriter, requestBuffer);
                    onSent();
                }
            } catch (InterruptedException interruptedException) {
            } catch (Exception e) {
                LogWrapper.d("NewSocketTest", "==SenderThread Exception:" + e);
                handleException(e, 2);
            } finally {
                closeSocket();
            }

        }
    }
}

