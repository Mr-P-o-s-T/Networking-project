package com.popovych.game.messages;

import com.popovych.game.interfaces.GameState;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class GameClientSyncMessage extends DefaultGameMessage{
    @Serial
    private static final long serialVersionUID = MessageType.GAME_CLIENT_SYNC.ordinal();

    protected GameState stateToSync;

    public GameClientSyncMessage(ClientData cData, GameState stateToSync) {
        super(MessageType.GAME_CLIENT_SYNC, cData);
        this.stateToSync = stateToSync;
    }

    public GameState getStateToSync() {
        return stateToSync;
    }

    public void setStateToSync(GameState stateToSync) {
        this.stateToSync = stateToSync;
    }
}
