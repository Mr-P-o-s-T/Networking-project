package com.popovych.networking.serverdatabase.serverhandler;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadSubgroupMaster;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientHandlerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServerHandlerThreadArguments;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServersHandlerThreadArguments;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseServersHandlerThread extends ThreadSubgroupMaster {
    protected ServerDatabase database;
    protected ServerSocket socket;

    private static Indexer<Integer> indexer;

    public DatabaseServersHandlerThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.databaseThread, Naming.Descriptions.serversHandlerThread, group,
                Naming.Groups.serverHandler, (DatabaseServersHandlerThread.indexer = indexer), false);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        DatabaseServersHandlerThreadArguments args = (DatabaseServersHandlerThreadArguments) arguments;
        this.database = args.getServerDatabase();
        this.socket = args.getServersSocket();
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        Socket server = null;
        try {
            server = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        try {
            SpawnWorker(new DatabaseServerHandlerThreadArguments(database, server));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finishTask() {

    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        DatabaseWorkerThreadType newThreadType = ((DatabaseWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == DatabaseWorkerThreadType.SERVER_HANDLER) {
            return new DatabaseServerHandlerThread(group, indexer, workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
