package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameServerActionAcceptedMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_SERVER_ACCEPTED.ordinal();

    public GameServerActionAcceptedMessage(ClientData cData) {
        super(MessageType.GAME_SERVER_ACCEPTED, cData);
    }
}
