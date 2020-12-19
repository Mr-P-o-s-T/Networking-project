package com.popovych.networking.abstracts.data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class NetData implements Serializable {
    protected InetAddress address;
    protected int port;

    public NetData(String IP, int port) throws UnknownHostException {
        this.address = InetAddress.getByName(IP);
        this.port = port;
    }

    protected NetData(InetAddress IP, int port) throws UnknownHostException {
        this.address = IP;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setAddress(String newIP) throws UnknownHostException {
        this.address = InetAddress.getByName(newIP);
    }

    public void setPort(int port) {
        this.port = port;
    }
}
