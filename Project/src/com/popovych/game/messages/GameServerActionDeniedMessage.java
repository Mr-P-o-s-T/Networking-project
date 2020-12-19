package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameServerActionDeniedMessage extends DefaultGameMessage {
    @Serial
    private static final long serialVersionUID = MessageType.GAME_SERVER_DENIED.ordinal();

    public GameServerActionDeniedMessage(ClientData cData) {
        super(MessageType.GAME_SERVER_DENIED, cData);
    }
}
