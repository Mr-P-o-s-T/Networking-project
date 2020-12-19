package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameServerSignalMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_SERVER_SIGNAL.ordinal();

    public GameServerSignalMessage(ClientData cData) {
        super(MessageType.GAME_SERVER_SIGNAL, cData);
    }
}
