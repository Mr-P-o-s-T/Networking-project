package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameClientUnregisterMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_CLIENT_UNREGISTER.ordinal();

    public GameClientUnregisterMessage(ClientData cData) {
        super(MessageType.GAME_CLIENT_UNREGISTER, cData);
    }
}
