package com.popovych.networking.serverdatabase.serverhandler;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.messages.ServerDatabaseInsertQueryMessage;
import com.popovych.networking.serverdatabase.serverhandler.args.DatabaseServerHandlerThreadArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DatabaseServerHandlerThread extends ThreadGroupWorker {
    protected Socket serverHandlerSocket;
    protected ServerDatabase serverDatabase;

    public DatabaseServerHandlerThread(ThreadGroup group, DatabaseServerHandlerThreadArguments args) {
        super(Naming.Templates.databaseThread, Naming.Descriptions.serverHandlerThread, group);
        serverDatabase = args.getServerDatabase();
        serverHandlerSocket = args.getClientHandlerSocket();
    }

    protected ServerDatabaseInsertQueryMessage getServerQuery() {
        ObjectInputStream in = null;
        ServerDatabaseInsertQueryMessage message = null;
        try {
            in = new ObjectInputStream(serverHandlerSocket.getInputStream());
            message = (ServerDatabaseInsertQueryMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }

        return message;
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        ServerDatabaseInsertQueryMessage query = getServerQuery();

        if (query.getType() == MessageType.SERVER_INSERT_QUERY) {
            serverDatabase.saveNewAvailableServerData(query.getSData());
        }
        else {
            serverDatabase.deleteAvailableServerData(query.getSData());
        }

        interrupt();
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
