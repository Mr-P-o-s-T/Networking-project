package com.popovych.networking.serverdatabase.serverhandler.args;

import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;

import java.net.ServerSocket;

public class DatabaseServersHandlerThreadArguments extends DatabaseWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATABASE(THREAD_TYPE),
        SERVERS_SOCKET(THREAD_TYPE);

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

    public DatabaseServersHandlerThreadArguments(ServerDatabase serverDatabase, ServerSocket serversSocket) {
        super(DatabaseWorkerThreadType.CLIENTS_HANDLER);
        args.add(serverDatabase);
        args.add(serversSocket);
    }

    public ServerSocket getServersSocket() {
        return (ServerSocket) args.get(ArgsType.SERVERS_SOCKET.getIndex());
    }

    public ServerDatabase getServerDatabase() {
        return (ServerDatabase) args.get(ArgsType.SERVER_DATABASE.getIndex());
    }
}
