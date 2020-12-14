package com.popovych.networking.serverdatabase.serverhandler;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadSubgroupMaster;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientHandlerArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;
import com.popovych.networking.serverdatabase.serverhandler.args.ServerHandlerArguments;
import com.popovych.networking.serverdatabase.serverhandler.args.ServersHandlerArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseServersHandlerArguments extends ThreadSubgroupMaster {
    protected ServerDatabase database;
    protected ServerSocket socket;

    protected DatabaseServersHandlerArguments(ThreadGroup group, ServersHandlerArguments args) {
        super(Naming.Templates.databaseThread, Naming.Descriptions.serversHandlerThread, group,
                Naming.Groups.serverHandler);
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
            SpawnWorker(new DatabaseClientHandlerArguments(database, server));
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
            return new DatabaseServerHandlerArguments(group, (ServerHandlerArguments) workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
