package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;

import java.net.UnknownHostException;

public class ServerDatabaseData extends NetData {

    public ServerDatabaseData(String IP, int port) throws UnknownHostException {
        super(IP, port);
    }
}
