package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;
import com.popovych.statics.Naming;

import java.net.UnknownHostException;
import java.util.Objects;

public class ClientData extends NetData {
    String name;
    String loggingPassword = "";

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

    public String getLoggingPassword() {
        return loggingPassword;
    }

    public void setLoggingPassword(String loggingPassword) {
        this.loggingPassword = loggingPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientData that = (ClientData) o;
        return (Objects.equals(name, that.name) && Objects.equals(loggingPassword, that.loggingPassword)) ||
                that.isBroadcast();
    }

    public boolean isBroadcast() {
        return Objects.equals(name, Naming.Constants.broadcast) && Objects.isNull(loggingPassword);
    }
}
