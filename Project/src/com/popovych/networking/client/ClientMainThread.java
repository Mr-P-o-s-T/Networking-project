package com.popovych.networking.client;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.MainThread;
import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.client.args.ClientMainThreadArguments;
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
import com.popovych.statics.Naming;

import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMainThread extends ThreadGroupMaster implements ServerDataProvider, MainThread {
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

    private static Indexer<Integer> indexer;

    public ClientMainThread(Game.ClientGame clientGame, ClientGameArguments clientArgs) throws UnknownHostException {
        this(new DefaultClientData(), new DefaultServerDatabaseData(), clientGame, clientArgs);
    }

    public ClientMainThread(ClientData cData, ServerDatabaseData sdbData, Game.ClientGame clientGame,
                            ClientGameArguments clientArgs) throws UnknownHostException {
        this(new ClientMainThreadArguments(cData, sdbData, clientGame, clientArgs));

    }

    public ClientMainThread(ClientMainThreadArguments args) {
        super(args, Naming.Templates.clientThread, Naming.Descriptions.mainThread, Naming.Groups.client, getIndexer(),
                false, false);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ClientMainThreadArguments args = (ClientMainThreadArguments) arguments;
        this.cData = args.getClientData();
        this.sdbData = args.getServerDatabaseData();
        this.clientGame = args.getClientGame();
        this.clientArgs = args.getClientGameArguments();
        this.clientArgs.setClientData(cData);
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void finishTask() {

    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        ClientWorkerThreadType newThreadType = ((ClientWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ClientWorkerThreadType.SERVER_SEARCHER) {
            return new ClientServerSearcherThread(group, indexer, workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.SERVER_TRANSMITTER) {
            return new ClientServerTransmitterThread(group, indexer, workerArgs);
        }
        else if (newThreadType == ClientWorkerThreadType.GAME) {
            return new ClientGameThread(group, indexer, workerArgs);
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

    public DatabaseController getDatabaseController() {
        return controller;
    }

    public ServerDataProvider getServerDataProvider() {
        return this;
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

    @Override
    public void SpawnGameThread() {
        try {
            SpawnWorkerMemo(new ClientGameThreadArguments(clientGame, clientArgs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SpawnDatabaseThread() {
        try {
            SpawnWorkerMemo(new ClientServerSearcherThreadArguments(cData, sdbData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SpawnTransmitterThread() {
        try {
            SpawnWorker(new ClientServerTransmitterThreadArguments(cData, controller,this, provider));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static synchronized Indexer<Integer> getIndexer() {
        return indexer = new Indexer<>() {
            @Override
            protected void initIndex() {
                index = 0;
            }

            @Override
            protected void updateIndex() {
                index++;
            }
        };
    }

    @Override
    public void interrupt() {
        group.interrupt();
    }
}
