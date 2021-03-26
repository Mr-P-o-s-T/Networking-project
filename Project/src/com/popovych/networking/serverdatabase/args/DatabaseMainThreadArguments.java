package com.popovych.networking.serverdatabase.args;

import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.serverdatabase.DatabaseWorkerThreadArguments;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;

import java.net.UnknownHostException;

public class DatabaseMainThreadArguments extends DatabaseWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATABASE_DATA(THREAD_TYPE),
        SERVER_DATABASE_RESPONSE_DATA(SERVER_DATABASE_DATA);

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

    public DatabaseMainThreadArguments() throws UnknownHostException {
        this(new DefaultServerDatabaseData());
    }

    public DatabaseMainThreadArguments(ServerDatabaseData sdData) throws UnknownHostException {
        super(DatabaseWorkerThreadType.MAIN_THREAD);
        args.add(sdData);
        args.add(new ServerDatabaseResponseData(sdData.getAddress(), sdData.getClientsHandlerPort(),
                sdData.getServersHandlerPort()));
    }

    public ServerDatabaseData getServerDatabaseData() {
        return (ServerDatabaseData) args.get(ArgsType.SERVER_DATABASE_DATA.getIndex());
    }

    public ServerDatabaseResponseData getServerDatabaseResponseData() {
        return (ServerDatabaseResponseData) args.get(ArgsType.SERVER_DATABASE_RESPONSE_DATA.getIndex());
    }
}
