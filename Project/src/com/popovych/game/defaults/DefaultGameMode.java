package com.popovych.game.defaults;

import com.popovych.game.args.GameModeArguments;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.interfaces.ActorSpawner;
import com.popovych.game.interfaces.GameMode;
import com.popovych.game.interfaces.GameState;

import java.lang.reflect.InvocationTargetException;

public class DefaultGameMode implements GameMode {
    protected final GameStateArguments gameStateArgs;
    protected Class<? extends GameState> gameStateClass;
    protected GameState sharedState = null;
    protected ActorSpawner spawner;

    public DefaultGameMode(GameModeArguments args) {
        this.gameStateClass = args.getGameStateClass();
        this.gameStateArgs = args.getGameStateArguments();
        this.spawner = args.getActorsSpawner();
    }

    @Override
    public void spawnActors() {
        spawner.spawnActors(sharedState);
    }

    @Override
    public void syncGameState(GameState stateToSync) {
        sharedState.synchronise(stateToSync);
        sharedState.update();
    }

    @Override
    public GameState getGameState() {
        if (sharedState == null) {
            try {
                sharedState = gameStateClass.getConstructor(GameStateArguments.class, Boolean.TYPE).newInstance(gameStateArgs, false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return sharedState;
    }
}
