package com.popovych.game;

import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;

import java.util.ArrayList;

public class GameModeArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        GAME_STATE_CLASS(0),
        GAME_STATE_ARGS(GAME_STATE_CLASS);

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

    public GameModeArguments(Class<? extends GameState> gameStateClass, GameStateArguments gsArgs) {
        super(new ArrayList<>());
        args.add(gameStateClass);
        args.add(gsArgs);
    }

    public Class<? extends GameState>getGameStateClass() {
        return ((Class<?>)args.get(ArgsType.GAME_STATE_CLASS.getIndex())).asSubclass(GameState.class);
    }

    public GameStateArguments getGameStateArguments() {
        return (GameStateArguments) args.get(ArgsType.GAME_STATE_ARGS.getIndex());
    }
}
