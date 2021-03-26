package com.popovych.game.defaults;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.game.interfaces.GameState;
import com.popovych.game.messages.*;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.lang.reflect.InvocationTargetException;

public class DefaultClientGame implements Game.ClientGame {
    boolean gameRunning;

    protected GameState currentState, stateSave;
    protected GameMessageTransmitter transmitter;
    protected ClientData cData;

    @Override
    public void initGame(ClientGameArguments args) {
        try {
            GameStateArguments gsArgs = args.getGameStateArguments();
            currentState = args.getGameStateClass().getConstructor(GameStateArguments.class).newInstance(new
                    GameStateArguments(gsArgs, true));
            stateSave = args.getGameStateClass().getConstructor(GameStateArguments.class).newInstance(
                    new GameStateArguments(gsArgs, false));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        transmitter = args.getMessageTransmitter();
        cData = args.getClientData();

        try {
            transmitter.sendMessage(new GameClientRegisterMessage(cData));
        } catch (InterruptedException e) {
            stopGame();
        }

        Message message;
        try {
            message = transmitter.receiveMessage();
        } catch (InterruptedException e) {
            return;
        }
        if (message.getType() != MessageType.GAME_SERVER_ACCEPTED) {
            try {
                transmitter.sendMessage(new GameClientUnregisterMessage(cData));
            } catch (InterruptedException ignored) { }
            stopGame();
            return;
        }
        try {
            transmitter.sendMessage(new GameClientWaitMessage(cData));
        } catch (InterruptedException e) {
            stopGame();
        }
    }

    @Override
    public boolean isGameRunning() {
        return gameRunning;
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
        try {
            transmitter.sendMessage(new GameClientUnregisterMessage(cData));
        } catch (InterruptedException ignored) { }
    }

    protected void handleWin() {
        if (currentState.signalWin()) {
            try {
                transmitter.sendMessage(new GameServerRestart(cData));
            } catch (InterruptedException e) {
                stopGame();
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void handleLose() {
        if (currentState.signalLose()) {
            try {
                transmitter.sendMessage(new GameServerRestart(cData));
            } catch (InterruptedException e) {
                stopGame();
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void handleStalemate() {
        if (currentState.signalStalemate()) {
            try {
                transmitter.sendMessage(new GameServerRestart(cData));
            } catch (InterruptedException e) {
                stopGame();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void gameCycleIteration() {
        Message message;
        try {
            message = transmitter.receiveMessage();
        } catch (InterruptedException ignored) {
            stopGame();
            Thread.currentThread().interrupt();
            return;
        }

        if (message.getType() == MessageType.GAME_SERVER_SYNC) {
            GameServerSyncMessage syncMessage = (GameServerSyncMessage) message;
            currentState.synchronise(syncMessage.getStateToSync());
            currentState.updateSave(stateSave);
            try {
                transmitter.sendMessage(new GameClientWaitMessage(cData));
            } catch (InterruptedException e) {
                stopGame();
                Thread.currentThread().interrupt();
            }
//            }
        }
        else if (message.getType() == MessageType.GAME_SERVER_SIGNAL) {
            currentState.getCurrentActor().act(currentState);
            try {
                transmitter.sendMessage(new GameClientSyncMessage(cData, currentState));
            } catch (InterruptedException e) {
                stopGame();
                Thread.currentThread().interrupt();
            }
        }
        else if (message.getType() == MessageType.GAME_CLIENT_WIN) {
            Platform.runLater(this::handleWin);
        }
        else if (message.getType() == MessageType.GAME_CLIENT_LOSE) {
            Platform.runLater(this::handleLose);
        }
        else if (message.getType() == MessageType.GAME_CLIENT_STALEMATE) {
            Platform.runLater(this::handleStalemate);
        }
        else if (message.getType() == MessageType.GAME_SERVER_DENIED) {
            currentState.restoreFrom(stateSave);
        }
    }
}
