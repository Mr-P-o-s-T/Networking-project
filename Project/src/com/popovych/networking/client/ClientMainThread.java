package com.popovych.networking.client;

import com.popovych.game.ClientGameArguments;
import com.popovych.game.Game;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.client.game.ClientGameThread;
import com.popovych.networking.client.game.args.ClientGameThreadArguments;
import com.popovych.networking.client.search.ClientServerSearcherThread;
import com.popovych.networking.client.search.args.ClientServerSearcherThreadArguments;
import com.popovych.networking.client.transmission.ClientServerTransmitterThread;
import com.popovych.networking.client.transmission.args.ClientServerTransmitterThreadArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.defaults.DefaultClientData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMainThread extends ThreadGroupMaster implements ServerDataProvider {
    protected ClientData cData;
    protected ServerData sData;
    protected ServerDatabaseData sdbData;
    protected DatabaseController controller;
    protected Game.ClientGame clientGame;
    protected ClientGameArguments clientArgs;

    protected ReentrantLock locker = new ReentrantLock();
    protected Condition chosenServer = locker.newCondition();
    protected boolean pickedUpServer = false;

    protected MessageQueueProvider provider;

    public ClientMainThread(Game.ClientGame clientGame, ClientGameArguments clientArgs) throws UnknownHostException {
        this(new DefaultClientData(), new DefaultServerDatabaseData(), clientGame, clientArgs);
    }

    public ClientMainThread(ClientData cData, ServerDatabaseData sdbData, Game.ClientGame clientGame,
                            ClientGameArguments clientArgs) throws UnknownHostException {
        super(Naming.Templates.clientThread, Naming.Descriptions.mainThread, Naming.Groups.client);
        this.cData = cData;
        this.sdbData = sdbData;
        this.clientGame = clientGame;
        this.clientArgs = clientArgs;
    }

    @Override
    protected void prepareTask() {
        try {
            SpawnWorkerMemo(new ClientServerSearcherThreadArguments(cData, sdbData));
            SpawnWorkerMemo(new ClientGameThreadArguments(clientGame, clientArgs));
            SpawnWorker(new ClientServerTransmitterThreadArguments(cData, controller,this, provider));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runTask() {
        join();

        interrupt();
    }

    @Override
    protected void finishTask() {

    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        ClientWorkerThreadType newThreadType = ((ClientWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ClientWorkerThreadType.SERVER_SEARCHER) {
            return new ClientServerSearcherThread(group, (ClientServerSearcherThreadArguments)workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.SERVER_TRANSMITTER) {
            return new ClientServerTransmitterThread(group, (ClientServerTransmitterThreadArguments) workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.GAME) {
            return new ClientGameThread(group, (ClientGameThreadArguments)workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {
        ClientWorkerThreadType newThreadType = ((ClientWorkerThreadArguments)workerArgs).getWorkerType();
        if (newThreadType == ClientWorkerThreadType.SERVER_SEARCHER) {
            controller = (DatabaseController) SpawnWorker(workerArgs);
        }
        else if ((newThreadType == ClientWorkerThreadType.GAME)) {
            provider = (ClientGameThread) SpawnWorker(workerArgs);
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
