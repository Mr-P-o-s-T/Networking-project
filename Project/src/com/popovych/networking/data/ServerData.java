package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;

import java.net.UnknownHostException;

public class ServerData extends NetData {
    String name;
    transient String password;

    public ServerData(String name, String password, String IP, int port) throws UnknownHostException {
        super(IP, port);
        this.name = name;
        this.password = password;
    }
}
