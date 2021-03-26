package com.popovych.networking.server.uncover.args;

import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerCoverThreadArguments extends ServerWorkerThreadArguments {
        private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATA(THREAD_TYPE),
        SERVER_DATABASE(SERVER_DATA);

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

    public ServerCoverThreadArguments(ServerData sData, ServerDatabaseData sdbData) {
        super(ServerWorkerThreadType.SERVER_COVER);
        args.add(sData);
        args.add(sdbData);
    }

    public ServerData getServerData() {
        return (ServerData) args.get(ArgsType.SERVER_DATA.getIndex());
    }

    public ServerDatabaseData getServerDatabaseData() {
        return (ServerDatabaseData) args.get(ArgsType.SERVER_DATABASE.getIndex());
    }
}
