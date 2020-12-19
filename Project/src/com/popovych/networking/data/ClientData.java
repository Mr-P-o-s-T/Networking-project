package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;
import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;
import java.util.Objects;

public class ClientData extends NetData {
    String name;

    public ClientData() throws UnknownHostException {
        super(Naming.Constants.localhost, 0);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientData that = (ClientData) o;
        return Objects.equals(name, that.name) || that.isBroadcast();
    }

    public boolean isBroadcast() {
        return Objects.equals(name, Naming.Constants.broadcast);
    }
}
