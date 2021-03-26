package com.popovych.networking.client.search;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.search.args.ClientServerSearcherThreadArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.interfaces.DatabaseControllerExecutor;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.messages.ClientDatabaseQueryMessage;
import com.popovych.networking.messages.ServerDatabaseResponseMessage;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientServerSearcherThread extends ThreadGroupWorker implements DatabaseControllerExecutor {
    protected ClientDatabaseQueryMessage query;
    protected ClientData cData;
    protected ServerDatabaseData sdbData;
    public ServerDatabaseResponseData resData;

    protected Lock locker = new ReentrantLock();
    protected Condition serverFoundCondition = null;
    protected boolean searchingNewServer = true;

    protected Socket serverDBSocket;

    public ClientServerSearcherThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.clientThread, Naming.Descriptions.serverSearcherThread, group, indexer, true);
    }

    protected void sendClientQuery(ClientDatabaseQueryMessage query) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(serverDBSocket.getOutputStream());
            out.writeObject(query);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    protected ServerDatabaseResponseMessage receiveServerResponse() {
        ObjectInputStream in = null;
        ServerDatabaseResponseMessage result = null;
        try {
            in = new ObjectInputStream(serverDBSocket.getInputStream());
            result = (ServerDatabaseResponseMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }

        return result;
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ClientServerSearcherThreadArguments args = (ClientServerSearcherThreadArguments) arguments;
        this.cData = args.getClientData();
        this.sdbData = args.getServerDatabaseData();
    }

    @Override
    protected void prepareTask() {
        try {
            serverDBSocket = new Socket(sdbData.getAddress(), sdbData.getClientsHandlerPort());
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
            return;
        }

        this.query = new ClientDatabaseQueryMessage(cData);
    }

    @Override
    protected void runTask() {
        locker.lock();
        try {
            sendClientQuery(query);

            resData = receiveServerResponse().getServerDatabaseResponseData();

            completeDatabaseAction();
            getDatabaseActionCompleteCondition().signalAll();
        } finally {
            locker.unlock();
        }
    }

    @Override
    protected void finishTask() {
        try {
            serverDBSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Lock getDatabaseControllerLocker() {
        return locker;
    }

    @Override
    public boolean databaseActionExecutionNow() {
        return searchingNewServer;
    }

    @Override
    public void completeDatabaseAction() {
        this.searchingNewServer = false;
    }

    @Override
    synchronized public Condition getDatabaseActionCompleteCondition() {
        if (serverFoundCondition == null) {
            serverFoundCondition = locker.newCondition();
        }
        return serverFoundCondition;
    }

    @Override
    public ServerDatabaseResponseData getServerDatabaseResponseData() {
        return resData;
    }
}
