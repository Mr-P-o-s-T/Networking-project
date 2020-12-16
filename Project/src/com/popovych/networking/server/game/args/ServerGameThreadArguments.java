package com.popovych.networking.server.game.args;

import com.popovych.game.Game;
import com.popovych.game.ServerGameArguments;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerGameThreadArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        GAME_CLASS(THREAD_TYPE),
        GAME_ARGS(GAME_CLASS);

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
    public ServerGameThreadArguments(Game.ServerGame game, ServerGameArguments sArgs) {
        super(ServerWorkerThreadType.GAME);
        args.add(game);
        args.add(sArgs);
    }

    public Game.ServerGame getGameClass() {
        return (Game.ServerGame) args.get(ArgsType.GAME_CLASS.getIndex());
    }

    public ServerGameArguments getGameArguments() {
        return (ServerGameArguments) args.get(ArgsType.GAME_ARGS.getIndex());
    }
}
