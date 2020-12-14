package com.popovych.networking.server;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

import java.util.ArrayList;

public class ServerWorkerThreadArguments extends DefaultArgumentsImplementation {
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

    public ServerWorkerThreadArguments(ServerWorkerThreadType type) {
        super(new ArrayList<Object>());
        args.add(type);
    }

    public ServerWorkerThreadType getWorkerType() {
        return (ServerWorkerThreadType)args.get(ArgsType.THREAD_TYPE.getIndex());
    }
}
