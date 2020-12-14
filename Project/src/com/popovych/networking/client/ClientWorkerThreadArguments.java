package com.popovych.networking.client;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;

import java.util.ArrayList;

public class ClientWorkerThreadArguments extends DefaultArgumentsImplementation {
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

    public ClientWorkerThreadArguments(ClientWorkerThreadType type) {
        super(new ArrayList<Object>());
        args.add(type);
    }

    public ClientWorkerThreadType getWorkerType() {
        return (ClientWorkerThreadType)args.get(ArgsType.THREAD_TYPE.getIndex());
    }
}
