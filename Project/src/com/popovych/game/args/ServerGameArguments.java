package com.popovych.game.args;

import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.game.interfaces.GameMode;
import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;

import java.util.ArrayList;

public class ServerGameArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        GAME_MODE_CLASS(0),
        GAME_MODE_ARGS(GAME_MODE_CLASS),
        GAME_MESSAGE_TRANSMITTER(GAME_MODE_ARGS);

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

    public ServerGameArguments(Class<? extends GameMode> gameModeClass) {
        this(gameModeClass, null);
    }

    public ServerGameArguments(Class<? extends GameMode> gameModeClass, GameModeArguments gmArgs) {
        super(new ArrayList<>());
        args.add(gameModeClass);
        args.add(gmArgs);
        args.add(null);
    }

    public Class<? extends GameMode>getGameModeClass() {
        return ((Class<?>)args.get(ArgsType.GAME_MODE_CLASS.getIndex())).asSubclass(GameMode.class);
    }

    public GameModeArguments getGameModeArguments() {
        return (GameModeArguments) args.get(ArgsType.GAME_MODE_ARGS.getIndex());
    }


    public void setGameMessageTransmitter(GameMessageTransmitter messageTransmitter) {
        args.set(ArgsType.GAME_MESSAGE_TRANSMITTER.getIndex(), messageTransmitter);
    }

    public GameMessageTransmitter getMessageTransmitter() {
        return (GameMessageTransmitter) args.get(ArgsType.GAME_MESSAGE_TRANSMITTER.getIndex());
    }

}
