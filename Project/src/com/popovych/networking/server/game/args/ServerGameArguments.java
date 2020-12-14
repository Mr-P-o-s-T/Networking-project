package com.popovych.networking.server.game.args;

import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerGameArguments extends ServerWorkerThreadArguments {
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
    public ServerGameArguments() {
        super(ServerWorkerThreadType.GAME);
    }
}
