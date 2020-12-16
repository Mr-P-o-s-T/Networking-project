package com.popovych.game.defaults;

import com.popovych.game.*;

import java.lang.reflect.InvocationTargetException;

public class DefaultGameMode implements GameMode {
    private final GameStateArguments gameStateArgs;
    protected Class<? extends GameState> gameStateClass;
    protected GameState sharedState = null;

    public DefaultGameMode() {
        this(new GameModeArguments(DefaultGameState.class, new GameStateArguments()));
    }

    public DefaultGameMode(GameModeArguments args) {
        this.gameStateClass = args.getGameStateClass();
        this.gameStateArgs = args.getGameStateArguments();
    }

    @Override
    public GameState getGameState() {
        if (sharedState == null) {
            try {
                sharedState = gameStateClass.getConstructor(GameStateArguments.class).newInstance(gameStateArgs);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return sharedState;
    }
}
