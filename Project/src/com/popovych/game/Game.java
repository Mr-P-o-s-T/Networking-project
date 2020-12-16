package com.popovych.game;

public class Game {
    public interface ClientGame {
        void initGame(ClientGameArguments args);
        void startGame();
        void finaliseGame();
    }

    public interface ServerGame {
        void initGame(ServerGameArguments args);
        void startGame();
        void finaliseGame();
    }
}
