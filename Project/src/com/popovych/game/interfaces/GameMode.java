package com.popovych.game.interfaces;

public interface GameMode {
    GameState getGameState();
    void spawnActors();

    void syncGameState(GameState stateToSync);
}
