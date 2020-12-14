package com.popovych.networking.serverdatabase;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;
import com.popovych.networking.serverdatabase.enumerations.DatabaseWorkerThreadType;

import java.util.ArrayList;

public class DatabaseWorkerThreadArguments extends DefaultArgumentsImplementation {
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

    public DatabaseWorkerThreadArguments(DatabaseWorkerThreadType type) {
        super(new ArrayList<Object>());
        args.add(type);
    }

    public DatabaseWorkerThreadType getWorkerType() {
        return (DatabaseWorkerThreadType) args.get(ArgsType.THREAD_TYPE.getIndex());
    }
}
