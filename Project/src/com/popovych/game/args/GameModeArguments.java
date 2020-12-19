package com.popovych.game.args;

import com.popovych.game.defaults.DefaultActorSpawner;
import com.popovych.game.interfaces.ActorSpawner;
import com.popovych.game.interfaces.GameState;
import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;

import java.util.ArrayList;

public class GameModeArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        GAME_STATE_CLASS(0),
        GAME_STATE_ARGS(GAME_STATE_CLASS),
        GAME_ACTOR_SPAWNER(GAME_STATE_ARGS);

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

    public GameModeArguments(Class<? extends GameState> gameStateClass, GameStateArguments gsArgs,
                             DefaultActorSpawner actorSpawner) {
        super(new ArrayList<>());
        args.add(gameStateClass);
        args.add(gsArgs);
        args.add(actorSpawner);
    }

    public Class<? extends GameState>getGameStateClass() {
        return ((Class<?>)args.get(ArgsType.GAME_STATE_CLASS.getIndex())).asSubclass(GameState.class);
    }

    public GameStateArguments getGameStateArguments() {
        return (GameStateArguments) args.get(ArgsType.GAME_STATE_ARGS.getIndex());
    }

    public ActorSpawner getActorsSpawner() {
        return (ActorSpawner) args.get(ArgsType.GAME_ACTOR_SPAWNER.getIndex());
    }
}
