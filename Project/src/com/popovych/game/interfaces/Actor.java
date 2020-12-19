package com.popovych.game.interfaces;

import com.popovych.networking.data.ClientData;

public interface Actor {
    void act(GameState state);

    boolean register(ClientData cData);
    ClientData getRegistered();
    void unregister();
}
