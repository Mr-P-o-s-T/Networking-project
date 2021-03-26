package com.popovych.networking.serverdatabase.serverhandler;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.messages.ServerDatabaseDeleteQueryMessage;
import com.popovych.networking.messages.ServerDatabaseInsertQueryMessage;
import com.popovych.networking.messages.ServerDatabaseQueryMessage;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServerHandlerThreadArguments;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DatabaseServerHandlerThread extends ThreadGroupWorker {
    protected Socket serverHandlerSocket;
    protected ServerDatabase serverDatabase;

    public DatabaseServerHandlerThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.databaseThread, Naming.Descriptions.serverHandlerThread, group, indexer,
                true);
    }

    protected ServerDatabaseQueryMessage getServerQuery() {
        ObjectInputStream in = null;
        ServerDatabaseQueryMessage message = null;
        try {
            in = new ObjectInputStream(serverHandlerSocket.getInputStream());
            message = (ServerDatabaseQueryMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }

        return message;
    }

    @Override
    protected void processArgs(Arguments arguments) {
        DatabaseServerHandlerThreadArguments args = (DatabaseServerHandlerThreadArguments) arguments;
        serverDatabase = args.getServerDatabase();
        serverHandlerSocket = args.getClientHandlerSocket();
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        ServerDatabaseQueryMessage query = getServerQuery();

        if (query.getType() == MessageType.SERVER_INSERT_QUERY) {
            ServerDatabaseInsertQueryMessage q = (ServerDatabaseInsertQueryMessage) query;
            serverDatabase.saveNewAvailableServerData(q.getServerData());
        }
        else if (query.getType() == MessageType.SERVER_DELETE_QUERY) {
            ServerDatabaseDeleteQueryMessage q = (ServerDatabaseDeleteQueryMessage) query;
            serverDatabase.deleteAvailableServerData(q.getServerData());
        }
    }

    @Override
    protected void finishTask() {
        try {
            serverHandlerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
