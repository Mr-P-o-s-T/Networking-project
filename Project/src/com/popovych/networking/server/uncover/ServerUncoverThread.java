package com.popovych.networking.server.uncover;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.interfaces.DatabaseControllerExecutor;
import com.popovych.networking.messages.ServerDatabaseInsertQueryMessage;
import com.popovych.networking.messages.ServerDatabaseQueryMessage;
import com.popovych.networking.server.uncover.args.ServerUncoverThreadArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerUncoverThread extends ThreadGroupWorker implements DatabaseControllerExecutor {
    protected ServerData sData;
    protected ServerDatabaseData sdbData;

    protected Lock locker = new ReentrantLock();
    protected Condition serverUpdateInfoCondition = null;
    protected boolean uncoverNewServer = true;

    protected Socket serverDBSocket;

    public ServerUncoverThread(ThreadGroup group, ServerUncoverThreadArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.serverUncoverThread, group);
        this.sData = args.getServerData();
        this.sdbData = args.getServerDatabaseData();
    }

    void sendServerQuery(ServerDatabaseQueryMessage query) {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(serverDBSocket.getOutputStream());
            out.writeObject(query);
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    @Override
    protected void prepareTask() {
        try {
            serverDBSocket = new Socket(sdbData.getAddress(), sdbData.getServersHandlerPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runTask() {
        sendServerQuery(new ServerDatabaseInsertQueryMessage(sData));

        interrupt();
    }

    @Override
    protected void finishTask() {
        try {
            serverDBSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean databaseActionExecutionNow() {
        return uncoverNewServer;
    }

    @Override
    public void completeDatabaseAction() {
        uncoverNewServer = false;
    }

    @Override
    public Lock getDatabaseControllerLocker() {
        return locker;
    }

    @Override
    public Condition getDatabaseActionCompleteCondition() {
        if (serverUpdateInfoCondition == null) {
            serverUpdateInfoCondition = locker.newCondition();
        }
        return serverUpdateInfoCondition;
    }
}
