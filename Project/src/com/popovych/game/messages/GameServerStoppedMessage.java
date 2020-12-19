package com.popovych.game.messages;

import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.defaults.DefaultBroadcastClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;
import java.net.UnknownHostException;

public class GameServerStoppedMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_SERVER_STOP.ordinal();

    public GameServerStoppedMessage() throws UnknownHostException {
        super(MessageType.GAME_SERVER_STOP, new DefaultBroadcastClientData());
    }
}
