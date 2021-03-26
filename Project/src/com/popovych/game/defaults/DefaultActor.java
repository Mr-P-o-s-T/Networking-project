package com.popovych.game.defaults;

import com.popovych.game.args.ActorArguments;
import com.popovych.game.interfaces.Actor;
import com.popovych.game.interfaces.GameState;
import com.popovych.networking.data.ClientData;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultActor implements Actor, Serializable {
    ClientData cData = null;

    boolean isActionDone = false;

    transient protected ReentrantLock actionLock = null;
    transient protected Condition actionLockCondition = null;

    protected final char mark;

    public DefaultActor(ActorArguments args) {
        mark = args.getMark();
    }

    public synchronized Condition getActionLockCondition() {
        if (actionLockCondition == null) {
            actionLockCondition = getActionLock().newCondition();
        }
        return actionLockCondition;
    }

    public synchronized ReentrantLock getActionLock() {
        if (actionLock == null) {
            actionLock = new ReentrantLock();
        }
        return actionLock;
    }

    public void makeAction() {
        isActionDone = true;
    }

    public void resetAction() {
        isActionDone = false;
    }

    public boolean isActionDone() {
        return isActionDone;
    }

    public char getMark() {
        return mark;
    }

    protected void changeGameState(GameState state) {
        DefaultGameState defaultGameState = (DefaultGameState) state;

        resetAction();
        getActionLock().lock();
        try {
            defaultGameState.activateBoard();

            while (!isActionDone)
                getActionLockCondition().await();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        finally {
            getActionLock().unlock();
            defaultGameState.deactivateBoard();
        }
    }

    @Override
    public void act(GameState state) {
        changeGameState(state);
    }

    public boolean register(ClientData cData) {
        if (this.cData == null) {
            this.cData = cData;
            return this.cData != null;
        }
        else
            return false;
    }

    @Override
    public ClientData getRegistered() {
        return cData;
    }

    @Override
    public void unregister() {
        this.cData = null;
    }
}
