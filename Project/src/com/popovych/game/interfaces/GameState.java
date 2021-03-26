package com.popovych.game.interfaces;

import com.popovych.networking.data.ClientData;

import java.io.Serializable;
import java.util.List;

public interface GameState extends Serializable {
    void synchronise(GameState newState);
    void update();
    void saveTo(GameState save);
    void updateSave(GameState save);
    void restoreFrom(GameState save);
    void resetScene();

    void addActor(Actor actor);
    void addActors(List<Actor> actors);
    Actor getCurrentActor();
    List<Actor> getActors();

    boolean canRegister();
    boolean registerActor(ClientData clientData);
    void unregisterActor(ClientData clientData);

    char checkWinState();
    boolean signalWin();
    boolean signalLose();
    boolean signalStalemate();
}
