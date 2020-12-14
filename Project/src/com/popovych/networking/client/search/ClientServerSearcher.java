package com.popovych.networking.client.search;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.search.args.ClientServerSearcherArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.interfaces.ServerSearchController;
import com.popovych.networking.messages.ClientDatabaseQueryMessage;
import com.popovych.networking.messages.ServerDatabaseResponseMessage;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientServerSearcher extends ThreadGroupWorker implements ServerSearchController {
    protected ClientDatabaseQueryMessage query;
    protected ClientData cData;
    protected ServerDatabaseData sdbData;
    public ServerDatabaseResponseData resData;

    protected Lock locker = new ReentrantLock();
    protected Condition serverFoundCondition = null;
    protected Condition serverSearchCondition = null;
    protected boolean searchNewServer = true;

    protected Socket serverDBSocket;

    public ClientServerSearcher(ThreadGroup group, ClientServerSearcherArguments args) {
        super(Naming.Templates.clientThread, Naming.Descriptions.serverSearcherThread, group);
        this.cData = args.getClientData();
        this.sdbData = args.getServerDatabaseData();
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
    protected void prepareTask() {
        try {
            serverDBSocket = new Socket(cData.getAddress(), cData.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        this.query = new ClientDatabaseQueryMessage(cData);
    }

    @Override
    protected void runTask() {
        locker.lock();
        try {
            if (!searchingNewServerNow())
                getServerSearchCondition().await();
            sendClientQuery(query);

            resData = receiveServerResponse().getSDRData();

            newServerFound();
            getServerFoundCondition().signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public Lock getServerSearchLocker() {
        return locker;
    }

    @Override
    public boolean searchingNewServerNow() {
        return searchNewServer;
    }

    public void searchNewServer() {
        this.searchNewServer = true;
    }

    @Override
    public void newServerFound() {
        this.searchNewServer = false;
    }

    public Condition getServerSearchCondition() {
        if (serverSearchCondition == null) {
            serverSearchCondition = locker.newCondition();
        }
        return serverSearchCondition;
    }

    synchronized public Condition getServerFoundCondition() {
        if (serverFoundCondition == null) {
            serverFoundCondition = locker.newCondition();
        }
        return serverFoundCondition;
    }
}
