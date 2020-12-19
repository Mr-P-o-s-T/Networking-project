package com.popovych.game.interfaces;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.args.ServerGameArguments;

public class Game {
    private interface BaseGame {
        void gameCycleIteration();
    }

    private interface GameFlowController {
        boolean isGameRunning();

        void startGame();
        void stopGame();
    }

    public interface ClientGame extends BaseGame, GameFlowController {
        void initGame(ClientGameArguments args);
        void finaliseGame();
    }

    public interface ServerGame extends BaseGame, GameFlowController {
        void initGame(ServerGameArguments args);
        void finaliseGame();
    }
}
