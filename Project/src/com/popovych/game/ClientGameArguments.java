package com.popovych.game;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;

import java.util.ArrayList;

public class ClientGameArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        GAME_STATE_CLASS(0);

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

    protected ClientGameArguments(Class<? extends GameState> gameStateClass) {
        super(new ArrayList<>());
        args.add(gameStateClass);
    }

    public Class<? extends GameState>getGameStateClass() {
        return (args.get(ArgsType.GAME_STATE_CLASS.getIndex())).getClass().asSubclass(GameState.class);
    }
}
