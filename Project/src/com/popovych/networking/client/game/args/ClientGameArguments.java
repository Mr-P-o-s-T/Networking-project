package com.popovych.networking.client.game.args;

import com.popovych.networking.client.ClientWorkerThreadArguments;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;

public class ClientGameArguments extends ClientWorkerThreadArguments {
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

    public ClientGameArguments() {
        super(ClientWorkerThreadType.GAME);
    }
}
