package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;

import java.net.UnknownHostException;

public class ClientData extends NetData {
    String name;

    public ClientData() throws UnknownHostException {
        super("localhost", 0);

    }

    public ClientData(String name, String IP, int port) throws UnknownHostException {
        super(IP, port);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
