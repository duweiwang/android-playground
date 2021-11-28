package com.example.wangduwei.project.socket.adress;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  14:48
 **/
public class Address {
    private String host;
    private int port;
    private int timeout;
    private String socketToken;
    private int protocolType;

    public Address(String host, int port, int timeout) {
        this(host, port, timeout, "");
    }

    public Address(String host, int port, int timeout, String socketToken) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.socketToken = socketToken;
    }

    public String getSocketToken() {
        return this.socketToken;
    }

    public void setSocketToken(String socketToken) {
        this.socketToken = socketToken;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public int getProtocolType() {
        return this.protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public String toString() {
        return "Address{host='" + this.host + '\'' + ", port=" + this.port + ", timeout=" + this.timeout + ", socketToken='" + this.socketToken + '\'' + ", protocolType=" + this.protocolType + '}';
    }
}

