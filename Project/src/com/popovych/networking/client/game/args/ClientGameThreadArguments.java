package com.popovych.networking.client.game.args;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.networking.client.ClientWorkerThreadArguments;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;

public class ClientGameThreadArguments extends ClientWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        GAME_CLASS(THREAD_TYPE),
        GAME_ARGS(GAME_CLASS);

        private final int index;
        private final int autoInc;

        ArgsType(ArgsType prevType) {
            this(prevType.index + prevType.autoInc, prevType.autoInc);
        }

        ArgsType(int i) {
            this(i, 1);
        }

        ArgsType(int i, int autoInc) {
            index = i;
            this.autoInc = autoInc;
        }

        public int getIndex() {
            return index;
        }
    }

    public ClientGameThreadArguments(Game.ClientGame game, ClientGameArguments cArgs) {
        super(ClientWorkerThreadType.GAME);
        args.add(game);
        args.add(cArgs);
    }

    public Game.ClientGame getGameClass() {
        return (Game.ClientGame) args.get(ArgsType.GAME_CLASS.getIndex());
    }

    public ClientGameArguments getGameArguments() {
        return (ClientGameArguments) args.get(ArgsType.GAME_ARGS.getIndex());
    }
}
