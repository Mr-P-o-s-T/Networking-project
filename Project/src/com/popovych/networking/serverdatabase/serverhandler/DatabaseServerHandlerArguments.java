package com.popovych.networking.serverdatabase.serverhandler;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.messages.ServerDatabaseQueryMessage;
import com.popovych.networking.messages.ServerDatabaseResponseMessage;
import com.popovych.networking.serverdatabase.serverhandler.args.ServerHandlerArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DatabaseServerHandlerArguments extends ThreadGroupWorker {
    protected Socket serverHandlerSocket;
    protected ServerDatabase serverDatabase;

    public DatabaseServerHandlerArguments(ThreadGroup group, ServerHandlerArguments args) {
        super(Naming.Templates.databaseThread, Naming.Descriptions.serverHandlerThread, group);
        serverDatabase = args.getServerDatabase();
        serverHandlerSocket = args.getClientHandlerSocket();
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
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        ServerDatabaseQueryMessage message = getServerQuery();

        if (message.getType() == MessageType.SERVER_INSERT_QUERY) {
            serverDatabase.saveNewAvailableServerData(message.getSData());
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
