package com.popovych.networking.server;

import com.popovych.game.Game;
import com.popovych.game.ServerGameArguments;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.defaults.DefaultServerData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;
import com.popovych.networking.server.game.ServerGameThread;
import com.popovych.networking.server.game.args.ServerGameThreadArguments;
import com.popovych.networking.server.transmission.ServerClientsHandlerThread;
import com.popovych.networking.server.transmission.args.ServerClientsHandlerThreadArguments;
import com.popovych.networking.server.uncover.ServerCoverThread;
import com.popovych.networking.server.uncover.ServerUncoverThread;
import com.popovych.networking.server.uncover.args.ServerCoverThreadArguments;
import com.popovych.networking.server.uncover.args.ServerUncoverThreadArguments;
import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;

public class ServerMainThread extends ThreadGroupMaster {
    protected ServerDatabaseData sdbData;
    protected ServerData sData;
    protected Game.ServerGame serverGame;
    protected ServerGameArguments serverArgs;

    protected MessageQueueProvider provider;

    public ServerMainThread(Game.ServerGame serverGame, ServerGameArguments serverArgs) throws UnknownHostException {
        this(new DefaultServerData(), new DefaultServerDatabaseData(), serverGame, serverArgs);
    }

    public ServerMainThread(ServerData sData, ServerDatabaseData sdbData, Game.ServerGame serverGame,
                            ServerGameArguments serverArgs) throws UnknownHostException {
        super(Naming.Templates.serverThread, Naming.Descriptions.mainThread, Naming.Groups.server);
        this.sData = sData;
        this.sdbData = sdbData;
        this.serverGame = serverGame;
        this.serverArgs = serverArgs;
    }

    @Override
    protected void prepareTask() {
        try {
            SpawnWorker(new ServerUncoverThreadArguments(sData, sdbData));
            SpawnWorkerMemo(new ServerGameThreadArguments(serverGame, serverArgs));
            SpawnWorker(new ServerClientsHandlerThreadArguments(sData, provider));
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
        try {
            SpawnWorkerMemo(new ServerCoverThreadArguments(sData, sdbData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        ServerWorkerThreadType newThreadType = ((ServerWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ServerWorkerThreadType.CLIENTS_HANDLER) {
            return new ServerClientsHandlerThread(group, (ServerClientsHandlerThreadArguments) workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.SERVER_UNCOVER) {
            return new ServerUncoverThread(group, (ServerUncoverThreadArguments) workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.SERVER_COVER) {
            return new ServerCoverThread(group, (ServerCoverThreadArguments) workerArgs);
        }
        else if (newThreadType == ServerWorkerThreadType.GAME) {
            return new ServerGameThread(group, (ServerGameThreadArguments) workerArgs);
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
            SpawnWorker(workerArgs).join();
        }
    }
}
