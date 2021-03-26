package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameClientWinMessage extends DefaultGameMessage {
    @Serial
    private static final long serialVersionUID = MessageType.GAME_CLIENT_WIN.ordinal();

    public GameClientWinMessage(ClientData cData) {
        super(MessageType.GAME_CLIENT_WIN, cData);
    }
}
