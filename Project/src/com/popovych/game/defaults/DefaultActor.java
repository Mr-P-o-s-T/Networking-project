package com.popovych.game.defaults;

import com.popovych.game.interfaces.Actor;
import com.popovych.game.interfaces.GameState;
import com.popovych.networking.data.ClientData;

public class DefaultActor implements Actor {
    ClientData cData = null;

    protected void changeGameState(GameState state) {
        state.signalChange();
    }

    @Override
    public void act(GameState state) {
        changeGameState(state);
    }

    public boolean register(ClientData cData) {
        if (this.cData == null)
            this.cData = cData;
        return this.cData == null;
    }

    @Override
    public ClientData getRegistered() {
        return cData;
    }

    @Override
    public void unregister() {
        this.cData = null;
    }
}
