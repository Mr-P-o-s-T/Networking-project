package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

public class GameServerRestart extends DefaultGameMessage {
    public GameServerRestart(ClientData cData) {
        super(MessageType.GAME_SERVER_RESTART, cData);
    }
}
