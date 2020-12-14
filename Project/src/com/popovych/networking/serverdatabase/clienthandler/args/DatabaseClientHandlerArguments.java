package com.popovych.networking.serverdatabase.clienthandler.args;

import com.popovych.networking.interfaces.ServerDatabase;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;

import java.net.Socket;

public class DatabaseClientHandlerArguments extends DatabaseWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATABASE(THREAD_TYPE),
        CLIENT_HANDLER_SOCKET(SERVER_DATABASE);

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

    public DatabaseClientHandlerArguments(ServerDatabase database, Socket clientHandlerSocket) {
        super(DatabaseWorkerThreadType.CLIENT_HANDLER);
        args.add(database);
        args.add(clientHandlerSocket);
    }

    public ServerDatabase getServerDatabase() {
        return (ServerDatabase) args.get(ArgsType.SERVER_DATABASE.getIndex());
    }

    public Socket getClientHandlerSocket() {
        return (Socket) args.get(ArgsType.CLIENT_HANDLER_SOCKET.getIndex());
    }
}
