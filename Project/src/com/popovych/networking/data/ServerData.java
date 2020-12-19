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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
