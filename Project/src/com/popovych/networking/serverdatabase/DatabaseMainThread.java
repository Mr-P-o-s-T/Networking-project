package com.popovych.networking.serverdatabase;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.clienthandler.DatabaseClientsHandlerThread;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientsHandlerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;
import com.popovych.networking.serverdatabase.serverhandler.DatabaseServerHandlerThread;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServerHandlerThreadArguments;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServersHandlerThreadArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class DatabaseMainThread extends ThreadGroupMaster implements ServerDatabase {

    protected ServerSocket clientsSocket;
    protected ServerSocket serversSocket;
    protected ServerDatabaseResponseData sdrData = new ServerDatabaseResponseData();

    protected DatabaseMainThread() throws UnknownHostException {
        super(Naming.Templates.databaseThread, Naming.Descriptions.mainThread, Naming.Groups.database);
    }

    @Override
    protected void prepareTask() {
        try {
            clientsSocket = new ServerSocket(sdrData.getPort());
            serversSocket = new ServerSocket(sdrData.getServersPort());
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
        try {
            SpawnWorker(new DatabaseClientsHandlerThreadArguments(this, clientsSocket));
            SpawnWorker(new DatabaseServersHandlerThreadArguments(this, serversSocket));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runTask() {

    }

    @Override
    protected void finishTask() {

    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        DatabaseWorkerThreadType newThreadType = ((DatabaseWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == DatabaseWorkerThreadType.CLIENTS_HANDLER) {
            return new DatabaseClientsHandlerThread(group, (DatabaseClientsHandlerThreadArguments) workerArgs);
        }
        else if (newThreadType == DatabaseWorkerThreadType.SERVERS_HANDLER) {
            return new DatabaseServerHandlerThread(group, (DatabaseServerHandlerThreadArguments) workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }

    public synchronized ServerDatabaseResponseData getSDRData() {
        return sdrData;
    }

    public synchronized void saveNewAvailableServerData(ServerData sData) {
        sdrData.addAvailableServerData(sData);
    }

    @Override
    public void deleteAvailableServerData(ServerData sData) {
        sdrData.removeAvailableServerData(sData);
    }
}
