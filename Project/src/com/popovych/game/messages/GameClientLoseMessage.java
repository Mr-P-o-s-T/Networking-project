package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameClientLoseMessage extends DefaultGameMessage {
    @Serial
    private static final long serialVersionUID = MessageType.GAME_CLIENT_LOSE.ordinal();

    public GameClientLoseMessage(ClientData cData) {
        super(MessageType.GAME_CLIENT_LOSE, cData);
    }
}
