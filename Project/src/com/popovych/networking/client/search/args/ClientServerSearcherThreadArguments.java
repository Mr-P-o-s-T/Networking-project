package com.popovych.networking.client.search.args;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.client.ClientWorkerThreadArguments;

public class ClientServerSearcherThreadArguments extends ClientWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_DATA(THREAD_TYPE),
        SERVER_DATABASE_DATA(CLIENT_DATA);

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

    public ClientServerSearcherThreadArguments(ClientData cData, ServerDatabaseData sdbData) {
        super(ClientWorkerThreadType.SERVER_SEARCHER);
        args.add(cData);
        args.add(sdbData);
    }

    public ClientData getClientData() {
        return (ClientData) args.get(ArgsType.CLIENT_DATA.getIndex());
    }

    public ServerDatabaseData getServerDatabaseData() {
        return (ServerDatabaseData) args.get(ArgsType.SERVER_DATABASE_DATA.getIndex());
    }
}
