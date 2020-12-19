package com.popovych.networking.data;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerDatabaseResponseData extends ServerDatabaseData{

    protected List<ServerData> availableServersData = new ArrayList<>();

    public ServerDatabaseResponseData(InetAddress databaseIP, int clientsHandlersPort, int serversHandlersPort) throws UnknownHostException {
        super(databaseIP, clientsHandlersPort, serversHandlersPort);
    }

    public ServerDatabaseResponseData(String IP, int clientsHandlersPort, int serversHandlersPort) throws UnknownHostException {
        super(IP, clientsHandlersPort, serversHandlersPort);
    }

    public List<ServerData> getAvailableServersData() {
        return availableServersData;
    }

    public void setAvailableServersData(List<ServerData> availableServersData) {
        this.availableServersData = availableServersData;
    }

    public void addAvailableServerData(ServerData availableServer) {
        availableServersData.add(availableServer);
    }

    public boolean removeAvailableServerData(ServerData availableServer) {
        return availableServersData.remove(availableServer);
    }

    public ServerData removeAvailableServerData(int index) {
        return availableServersData.remove(index);
    }
}
