package com.popovych.game.defaults;

import com.popovych.game.interfaces.Actor;
import com.popovych.game.interfaces.GameState;
import com.popovych.game.args.GameStateArguments;
import com.popovych.networking.data.ClientData;

import java.util.ArrayList;
import java.util.List;

public class DefaultGameState implements GameState {
    boolean isChanged = false;

    List<Actor> actors = new ArrayList<>();

    int currentActorNum = 0;

    public DefaultGameState(GameStateArguments args) {

    }

    @Override
    public synchronized boolean isChanged() {
        return isChanged;
    }

    @Override
    public synchronized void signalChange() {
        isChanged = true;
    }

    @Override
    public void synchronise(GameState newState) {
        if (isChanged()) {
            currentActorNum = (currentActorNum + 1) % actors.size();
        }
    }

    @Override
    public void addActor(Actor actor) {
        actors.add(actor);
    }

    @Override
    public void addActors(List<Actor> actors) {
        this.actors.addAll(actors);
    }

    @Override
    public Actor getCurrentActor() {
        return actors.get(currentActorNum);
    }

    @Override
    public boolean registerActor(ClientData cData) {
        int index = 0;
        for (Actor actor : actors) {
            if (actor.getRegistered() == null) {
                return actor.register(cData);
            }
        }
        return false;
    }

    @Override
    public void unregisterActor(ClientData clientData) {
        for (Actor actor : actors) {
            if (actor.getRegistered() == clientData) {
                actor.unregister();
                break;
            }
        }
    }
}
