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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerData that = (ServerData) o;
        if (that.password == null && password == null) {
            return (that.name.equals(name)) && (that.address.toString().equals(address.toString())) &&
                    (that.port == port);
        }
        else if (that.password != null && password != null) {
            return (that.name.equals(name)) && (that.password.equals(password)) &&
                    (that.address.toString().equals(address.toString())) && (that.port == port);
        }
        else
            return false;
    }
}
