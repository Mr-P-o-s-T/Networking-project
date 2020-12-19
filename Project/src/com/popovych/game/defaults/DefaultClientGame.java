package com.popovych.game.defaults;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.game.interfaces.GameState;
import com.popovych.game.messages.*;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;

import java.lang.reflect.InvocationTargetException;

public class DefaultClientGame implements Game.ClientGame {
    boolean gameRunning;

    protected GameState currentState, changeableState;
    protected GameMessageTransmitter transmitter;
    protected ClientData cData;

    @Override
    public void initGame(ClientGameArguments args) {
        try {
            GameStateArguments gsArgs = args.getGameStateArguments();
            currentState = args.getGameStateClass().getConstructor(GameStateArguments.class).newInstance(gsArgs);
            changeableState = args.getGameStateClass().getConstructor(GameStateArguments.class).newInstance(gsArgs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        transmitter = args.getMessageTransmitter();
        cData = args.getClientData();

        transmitter.sendMessage(new GameClientRegisterMessage(cData));
        Message message = transmitter.receiveMessage();
        if (message.getType() == MessageType.GAME_SERVER_SYNC) {
            GameServerSyncMessage syncMessage = (GameServerSyncMessage) message;
            currentState.synchronise(syncMessage.getStateToSync());
            changeableState.synchronise(currentState);
        }
        else if (message.getType() == MessageType.GAME_SERVER_DENIED) {
            //GameServerActionDeniedMessage deniedMessage = (GameServerActionDeniedMessage) message;
            transmitter.sendMessage(new GameClientUnregisterMessage(cData));
            stopGame();
            return;
        }
        transmitter.sendMessage(new GameClientWaitMessage(cData));
    }

    @Override
    public boolean isGameRunning() {
        return false;
    }

    @Override
    public synchronized void startGame() {
        gameRunning = true;
    }

    @Override
    public synchronized void stopGame() {
        gameRunning = false;
    }

    @Override
    public synchronized void finaliseGame() {
        transmitter.sendMessage(new GameClientUnregisterMessage(cData));
    }

    @Override
    public void gameCycleIteration() {
        Message message = transmitter.receiveMessage();

        if (message.getType() == MessageType.GAME_SERVER_SYNC ) {
            GameServerSyncMessage syncMessage = (GameServerSyncMessage) message;
            currentState.synchronise(syncMessage.getStateToSync());
            if (currentState.getCurrentActor().getRegistered() == cData) {
                changeableState.getCurrentActor().act(changeableState);
                transmitter.sendMessage(new GameClientSyncMessage(cData, changeableState));
            }
            else {
                transmitter.sendMessage(new GameClientWaitMessage(cData));
            }
        }
        else if (message.getType() == MessageType.GAME_SERVER_SIGNAL) {
            changeableState.getCurrentActor().act(changeableState);
            transmitter.sendMessage(new GameClientSyncMessage(cData, changeableState));
        }
        else if (message.getType() == MessageType.GAME_SERVER_DENIED) {
            changeableState.synchronise(currentState);
        }
    }
}
