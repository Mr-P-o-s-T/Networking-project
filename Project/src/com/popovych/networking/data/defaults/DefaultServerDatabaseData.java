package com.popovych.networking.data.defaults;

import com.popovych.networking.data.ServerDatabaseData;

import java.net.UnknownHostException;

public class DefaultServerDatabaseData extends ServerDatabaseData {
    public DefaultServerDatabaseData() throws UnknownHostException {
        this("localhost", 1490);
    }

    public DefaultServerDatabaseData(String IP, int port) throws UnknownHostException {
        super(IP, port);
    }
}
