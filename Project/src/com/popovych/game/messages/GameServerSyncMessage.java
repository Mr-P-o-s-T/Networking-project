package com.popovych.game.messages;

import com.popovych.game.interfaces.GameState;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.defaults.DefaultBroadcastClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;
import java.net.UnknownHostException;

public class GameServerSyncMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_SERVER_SYNC.ordinal();

    protected GameState stateToSync = null;

    public GameServerSyncMessage() {
        super(MessageType.NONE, null);
    }

    public GameServerSyncMessage(ClientData cData, GameState stateToSync) {
        super(MessageType.GAME_SERVER_SYNC, cData);
        this.stateToSync = stateToSync;
    }

    public GameServerSyncMessage(GameState stateToSync) throws UnknownHostException {
        super(MessageType.GAME_SERVER_SYNC, new DefaultBroadcastClientData());
        this.stateToSync = stateToSync;
    }

    public GameState getStateToSync() {
        return stateToSync;
    }

    public void setStateToSync(GameState stateToSync) {
        this.stateToSync = stateToSync;
    }
}
