package com.popovych.networking.server.uncover.args;

import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerUncoverArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0);

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
    public ServerUncoverArguments(ServerWorkerThreadType type) {
        super(type);
    }
}
