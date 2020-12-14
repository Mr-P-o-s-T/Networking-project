package com.popovych.networking.serverdatabase.clienthandler.args;

import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;

import java.net.ServerSocket;

public class DatabaseClientsHandlerArguments extends DatabaseWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATABASE(THREAD_TYPE),
        CLIENTS_SOCKET(SERVER_DATABASE);

        private final int index;
        private final int autoinc;

        ArgsType(ArgsType prevType) {
            this(prevType.index + prevType.autoinc, prevType.autoinc);
        }

        ArgsType(int i) {
            this(i, 1);
        }

        ArgsType(int i, int autoinc) {
            index = i;
            this.autoinc = autoinc;
        }

        public int getIndex() {
            return index;
        }
    }

    public DatabaseClientsHandlerArguments(ServerDatabase database, ServerSocket clientsSocket) {
        super(DatabaseWorkerThreadType.CLIENTS_HANDLER);
        args.add(database);
        args.add(clientsSocket);
    }

    public ServerDatabase getServerDatabase() {
        return (ServerDatabase) args.get(ArgsType.SERVER_DATABASE.getIndex());
    }

    public ServerSocket getClientsSocket() {
        return (ServerSocket) args.get(ArgsType.CLIENTS_SOCKET.getIndex());
    }
}
