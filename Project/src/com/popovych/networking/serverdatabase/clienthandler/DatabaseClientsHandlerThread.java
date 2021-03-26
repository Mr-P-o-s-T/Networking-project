package com.popovych.networking.serverdatabase.clienthandler;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadSubgroupMaster;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientHandlerThreadArguments;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientsHandlerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseClientsHandlerThread extends ThreadSubgroupMaster {

    ServerSocket socket;
    ServerDatabase database;

    private static Indexer<Integer> indexer;

    public DatabaseClientsHandlerThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.databaseThread, Naming.Descriptions.clientsHandlerThread, group,
                Naming.Groups.clientHandler, (DatabaseClientsHandlerThread.indexer = indexer), false);

    }

    @Override
    protected void processArgs(Arguments arguments) {
        DatabaseClientsHandlerThreadArguments args = (DatabaseClientsHandlerThreadArguments) arguments;
        database = args.getServerDatabase();
        socket = args.getClientsSocket();
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        Socket client = null;
        try {
            client = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        try {
            SpawnWorker(new DatabaseClientHandlerThreadArguments(database, client));
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

        if (newThreadType == DatabaseWorkerThreadType.CLIENT_HANDLER) {
            return new DatabaseClientHandlerThread(group, indexer, workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
