package com.popovych.game.defaults;

import com.popovych.game.Game;
import com.popovych.game.GameMode;
import com.popovych.game.GameModeArguments;
import com.popovych.game.ServerGameArguments;

import java.lang.reflect.InvocationTargetException;

public class DefaultServerGame implements Game.ServerGame {
    protected GameMode gameMode;

    @Override
    public void initGame(ServerGameArguments args) {
        try {
            GameModeArguments gmArgs = args.getGameModeArguments();
            if (gmArgs == null) {
                gameMode = args.getGameModeClass().getConstructor().newInstance();
            }
            else {
                gameMode = args.getGameModeClass().getConstructor(GameModeArguments.class).newInstance(gmArgs);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void startGame() {

    }

    @Override
    public void finaliseGame() {

    }
}
