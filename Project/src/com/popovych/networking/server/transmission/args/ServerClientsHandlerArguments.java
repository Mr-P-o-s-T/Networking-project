package com.popovych.networking.server.transmission.args;

import com.popovych.networking.data.ServerData;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerClientsHandlerArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATA(THREAD_TYPE);

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

    public ServerClientsHandlerArguments(ServerData sData) {
        super(ServerWorkerThreadType.CLIENTS_HANDLER);
        args.add(sData);
    }

    public ServerData getSData() {
        return (ServerData) args.get(ArgsType.SERVER_DATA.getIndex());
    }
}
