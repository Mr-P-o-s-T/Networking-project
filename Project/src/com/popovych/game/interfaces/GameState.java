package com.popovych.game.interfaces;

import com.popovych.networking.data.ClientData;

import java.io.Serializable;
import java.util.List;

public interface GameState extends Serializable {
    boolean isChanged();
    void signalChange();
    void synchronise(GameState newState);

    void addActor(Actor actor);
    void addActors(List<Actor> actors);
    Actor getCurrentActor();
    boolean registerActor(ClientData clientData);
    void unregisterActor(ClientData clientData);
}
