package com.popovych.game.defaults;

import com.popovych.game.args.ActorArguments;
import com.popovych.game.defaults.DefaultActor;
import com.popovych.game.interfaces.Actor;
import com.popovych.game.interfaces.ActorSpawner;
import com.popovych.game.interfaces.GameState;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DefaultActorSpawner implements ActorSpawner {
    ActorSpawnInfo spawnInfoPlayer1 = new ActorSpawnInfo(DefaultActor.class, new ActorArguments());
    ActorSpawnInfo spawnInfoPlayer2 = new ActorSpawnInfo(DefaultActor.class, new ActorArguments());

    public DefaultActorSpawner() {
    }

    @Override
    public void spawnActors(GameState state) {
        state.addActor(spawnNewActor(spawnInfoPlayer1));
        state.addActor(spawnNewActor(spawnInfoPlayer2));
    }

    protected static class ActorSpawnInfo {
        protected final Class<? extends Actor> actorClass;
        protected final int actorNumber;
        private final ActorArguments aArgs;

        public ActorSpawnInfo(Class<? extends Actor> actorClass, ActorArguments aArgs) {
            this(actorClass, aArgs, 1);
        }

        public ActorSpawnInfo(Class<? extends Actor> actorClass, ActorArguments aArgs, int actorNumber) {
            this.actorClass = actorClass;
            this.aArgs = aArgs;
            this.actorNumber = actorNumber;
        }

        public Class<? extends Actor> getActorClass() {
            return actorClass;
        }

        public ActorArguments getActorArguments() {
            return aArgs;
        }

        public int getActorNumber() {
            return actorNumber;
        }
    }

    protected Actor spawnNewActor(ActorSpawnInfo info) {
        try {
            return info.getActorClass().getConstructor(ActorArguments.class).newInstance(info.getActorArguments());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<Actor> spawnNewActors(ActorSpawnInfo info) {
        List<Actor> spawnedActors = new ArrayList<>();
        for (int i = 0; i < info.getActorNumber(); i++) {
            spawnedActors.add(spawnNewActor(info));
        }
        return spawnedActors;
    }
}
