package com.popovych.networking.data.defaults;

import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.statics.Naming;

import java.net.UnknownHostException;

public class DefaultServerDatabaseData extends ServerDatabaseData {
    public DefaultServerDatabaseData() throws UnknownHostException {
        this(Naming.Constants.defaultDatabaseIP, Naming.Constants.defaultDatabaseCPort,
                Naming.Constants.getDefaultDatabaseSPort);
    }

    public DefaultServerDatabaseData(String IP, int clientsHandlerPort, int serversHandlerPort) throws UnknownHostException {
        super(IP, clientsHandlerPort, serversHandlerPort);
    }
}
