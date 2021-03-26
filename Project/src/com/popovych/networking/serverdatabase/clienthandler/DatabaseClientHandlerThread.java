package com.popovych.networking.serverdatabase.clienthandler;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.messages.ClientDatabaseQueryMessage;
import com.popovych.networking.messages.ServerDatabaseResponseMessage;
import com.popovych.networking.serverdatabase.clienthandler.args.DatabaseClientHandlerThreadArguments;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DatabaseClientHandlerThread extends ThreadGroupWorker {
    protected Socket clientHandlerSocket;
    protected ServerDatabase serverDatabase;

    public DatabaseClientHandlerThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.databaseThread, Naming.Descriptions.clientHandlerThread, group, indexer,
                true);
    }

    protected void sendServerResponse(ServerDatabaseResponseMessage message) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(clientHandlerSocket.getOutputStream());
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    protected ClientDatabaseQueryMessage getClientQuery() {
        ObjectInputStream in = null;
        ClientDatabaseQueryMessage message = null;
        try {
            in = new ObjectInputStream(clientHandlerSocket.getInputStream());
            message = (ClientDatabaseQueryMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }

        return message;
    }

    @Override
    protected void processArgs(Arguments arguments) {
        DatabaseClientHandlerThreadArguments args = (DatabaseClientHandlerThreadArguments) arguments;
        serverDatabase = args.getServerDatabase();
        clientHandlerSocket = args.getClientHandlerSocket();
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {
        ClientDatabaseQueryMessage query = getClientQuery();

        ServerDatabaseResponseMessage message;
        if (query.getType() == MessageType.CLIENT_SERVERS_QUERY) {
            message = new ServerDatabaseResponseMessage(serverDatabase.getSDRData());
        }
        else {
             message = new ServerDatabaseResponseMessage();
        }
        sendServerResponse(message);
    }

    @Override
    protected void finishTask() {
        try {
            clientHandlerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
