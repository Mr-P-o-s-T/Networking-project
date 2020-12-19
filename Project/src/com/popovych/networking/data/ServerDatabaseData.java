package com.popovych.networking.data;

import com.popovych.networking.abstracts.data.NetData;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerDatabaseData extends NetData {
    int serversHandlerPort;

    protected ServerDatabaseData(InetAddress databaseIP, int clientsHandlerPort, int serversHandlerPort) throws UnknownHostException {
        super(databaseIP, clientsHandlerPort);
        this.serversHandlerPort = serversHandlerPort;
    }

    public ServerDatabaseData(String IP, int clientsHandlerPort, int serversHandlerPort) throws UnknownHostException {
        super(IP, clientsHandlerPort);
        this.serversHandlerPort = serversHandlerPort;
    }

    public int getClientsHandlerPort() {
        return getPort();
    }

    public void setClientsHandlerPort(int serversHandlerPort) {
        setPort(serversHandlerPort);
    }

    public int getServersHandlerPort() {
        return serversHandlerPort;
    }

    public void setServersHandlerPort(int serversHandlerPort) {
        this.serversHandlerPort = serversHandlerPort;
    }
}
