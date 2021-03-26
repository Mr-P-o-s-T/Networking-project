package com.popovych.game.args;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;

import java.util.ArrayList;

public class ActorArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        ACTOR_MARK(0);

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

    public ActorArguments(char mark) {
        super(new ArrayList<>());
        args.add(mark);
    }

    public char getMark() {
        return (char) args.get(ArgsType.ACTOR_MARK.getIndex());
    }
}
