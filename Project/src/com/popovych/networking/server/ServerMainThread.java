package com.popovych.networking.server;

import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.ClientDataContainer;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.MainThread;
import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.server.args.ServerMainThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;
import com.popovych.networking.server.game.ServerGameThread;
import com.popovych.networking.server.game.args.ServerGameThreadArguments;
import com.popovych.networking.server.transmission.ServerClientsHandlerThread;
import com.popovych.networking.server.transmission.args.ServerClientsHandlerThreadArguments;
import com.popovych.networking.server.uncover.ServerCoverThread;
import com.popovych.networking.server.uncover.ServerUncoverThread;
import com.popovych.networking.server.uncover.args.ServerCoverThreadArguments;
import com.popovych.networking.server.uncover.args.ServerUncoverThreadArguments;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerMainThread extends ThreadGroupMaster implements MainThread {
    protected ServerDatabaseData sdbData;
    protected ServerData sData;
    protected Game.ServerGame serverGame;
    protected ServerGameArguments serverArgs;

    protected MessageQueueProvider provider;

    private static Indexer<Integer> indexer;

    public ServerMainThread(ServerData sData, ServerDatabaseData sdbData, Game.ServerGame serverGame,
                            ServerGameArguments serverArgs) throws UnknownHostException {
        this(new ServerMainThreadArguments(sData, sdbData, serverGame, serverArgs));
    }

    public ServerMainThread(Arguments args) {
        super(args, Naming.Templates.serverThread, Naming.Descriptions.mainThread, Naming.Groups.server, getIndexer(),
                false, true);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ServerMainThreadArguments args = (ServerMainThreadArguments) arguments;
        this.sData = args.getServerData();
        this.sdbData = args.getServerDatabaseData();
        this.serverGame = args.getServerGame();
        this.serverArgs = args.getServerGameArguments();
    }

    @Override
    protected void prepareTask() {
        SpawnDatabaseThread();
    }

    @Override
    protected void finishTask() {
        try {
            SpawnWorkerMemo(new ServerCoverThreadArguments(sData, sdbData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        Socket switchOff;
        try {
            switchOff = new Socket(sData.getAddress(), sData.getPort());
            switchOff.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        ServerWorkerThreadType newThreadType = ((ServerWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ServerWorkerThreadType.CLIENTS_HANDLER) {
            return new ServerClientsHandlerThread(group, indexer, workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.SERVER_UNCOVER) {
            return new ServerUncoverThread(group, indexer, workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.SERVER_COVER) {
            return new ServerCoverThread(group, indexer, workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.GAME) {
            return new ServerGameThread(group, indexer, workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {
        ServerWorkerThreadType newThreadType = ((ServerWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ServerWorkerThreadType.GAME) {
            provider = (MessageQueueProvider) SpawnWorker(workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.SERVER_COVER) {
            SpawnWorker(workerArgs);
        }
    }

    @Override
    public void SpawnGameThread() {
        try {
            SpawnWorkerMemo(new ServerGameThreadArguments(serverGame, serverArgs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SpawnDatabaseThread() {
        try {
            SpawnWorker(new ServerUncoverThreadArguments(sData, sdbData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SpawnTransmitterThread() {
        try {
            SpawnWorker(new ServerClientsHandlerThreadArguments(sData, provider, (ClientDataContainer) provider));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DatabaseController getDatabaseController() {
        return null;
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
}
