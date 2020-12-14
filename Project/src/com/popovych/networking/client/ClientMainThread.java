package com.popovych.networking.client;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.client.game.ClientGameThread;
import com.popovych.networking.client.game.args.ClientGameArguments;
import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.client.search.ClientServerSearcher;
import com.popovych.networking.client.search.args.ClientServerSearcherArguments;
import com.popovych.networking.client.transmission.ClientServerTransmitter;
import com.popovych.networking.client.transmission.args.ClientServerTransmitterArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.defaults.DefaultClientData;
import com.popovych.networking.data.defaults.DefaultServerData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.interfaces.ServerSearchController;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMainThread extends ThreadGroupMaster implements ServerDataProvider {
    protected ClientData cData = new DefaultClientData();
    protected ServerData sData = new DefaultServerData();
    protected ServerDatabaseData sdbData = new DefaultServerDatabaseData();
    protected ServerSearchController controller;

    protected ReentrantLock locker = new ReentrantLock();
    protected Condition chosenServer = locker.newCondition();
    protected boolean pickedUpServer = false;

    protected ClientMessageQueue inputQueue = new ClientMessageQueue(), outputQueue = new ClientMessageQueue();

    public ClientMainThread() throws UnknownHostException {
        super(Naming.Templates.clientThread, Naming.Descriptions.mainThread, Naming.Groups.client);
    }

    @Override
    protected void prepareTask() {
        try {
            SpawnWorker(new ClientServerSearcherArguments(cData, sdbData));
            SpawnWorker(new ClientServerTransmitterArguments(cData, controller,this, inputQueue, outputQueue));
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
        ClientWorkerThreadType newThreadType = ((ClientWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ClientWorkerThreadType.SERVER_SEARCHER) {
            return new ClientServerSearcher(group, (ClientServerSearcherArguments)workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.SERVER_TRANSMITTER) {
            return new ClientServerTransmitter(group, (ClientServerTransmitterArguments) workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.GAME) {
            return new ClientGameThread(group, (ClientGameArguments)workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {
        ClientWorkerThreadType newThreadType = ((ClientWorkerThreadArguments)workerArgs).getWorkerType();
        if (newThreadType == ClientWorkerThreadType.SERVER_SEARCHER) {
            controller = (ServerSearchController) SpawnWorker(workerArgs);
        }
    }

    @Override
    public boolean isServerChosen() {
        return pickedUpServer;
    }

    @Override
    public void serverChosen() {
        pickedUpServer = true;
    }

    @Override
    public void serverReset() {
        pickedUpServer = false;
    }

    @Override
    public void setCurrentServerData(ServerData sData) {
        this.sData = sData;
    }

    @Override
    public ServerData getServerData() {
        return sData;
    }

    @Override
    public Lock getChosenServerLocker() {
        return locker;
    }

    @Override
    public Condition getChosenServerCondition() {
        return chosenServer;
    }
}
