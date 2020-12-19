package com.popovych.game.args;

import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.game.interfaces.GameState;
import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;
import com.popovych.networking.data.ClientData;

import java.util.ArrayList;

public class ClientGameArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        GAME_STATE_CLASS(0),
        GAME_STATE_ARGS(GAME_STATE_CLASS),
        GAME_MESSAGE_TRANSMITTER(GAME_STATE_ARGS),
        CLIENT_DATA(GAME_MESSAGE_TRANSMITTER);

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

    public ClientGameArguments(Class<? extends GameState> gameStateClass, GameStateArguments gsArgs, ClientData clientData) {
        super(new ArrayList<>());
        args.add(gameStateClass);
        args.add(gsArgs);
        args.add(null);
        args.add(clientData);
    }

    public Class<? extends GameState> getGameStateClass() {
        return (args.get(ArgsType.GAME_STATE_CLASS.getIndex())).getClass().asSubclass(GameState.class);
    }

    public GameStateArguments getGameStateArguments() {
        return (GameStateArguments) args.get(ArgsType.GAME_STATE_ARGS.getIndex());
    }

    public void setGameMessageTransmitter(GameMessageTransmitter messageTransmitter) {
        args.set(ArgsType.GAME_MESSAGE_TRANSMITTER.getIndex(), messageTransmitter);
    }

    public GameMessageTransmitter getMessageTransmitter() {
        return (GameMessageTransmitter) args.get(ArgsType.GAME_MESSAGE_TRANSMITTER.getIndex());
    }

    public ClientData getClientData() {
        return (ClientData) args.get(ArgsType.CLIENT_DATA.getIndex());
    }
}
