package com.popovych.game.interfaces;

import com.popovych.networking.interfaces.DatabaseController;

public interface MainThread {
    void SpawnGameThread();
    void SpawnDatabaseThread();
    void SpawnTransmitterThread();

    DatabaseController getDatabaseController();

    void interrupt();
}
