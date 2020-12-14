package com.popovych.networking.data;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerDatabaseResponseData extends ServerDatabaseData{

    protected List<ServerData> availableServersData = new ArrayList<>();

    int serversPort;

    public ServerDatabaseResponseData() throws UnknownHostException {
        this("localhost", 0, 0);
    }

    public ServerDatabaseResponseData(String IP, int clientsPort, int serversPort) throws UnknownHostException {
        super(IP, clientsPort);
        this.serversPort = serversPort;
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

    public void concatenate(ServerDatabaseResponseData sdbData) {
        availableServersData.addAll(sdbData.availableServersData);
    }

    public int getServersPort() {
        return serversPort;
    }

    public void setServersPort(int serversPort) {
        this.serversPort = serversPort;
    }
}
