package com.popovych.game.defaults;

import com.popovych.game.args.GameModeArguments;
import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.game.interfaces.GameMode;
import com.popovych.game.interfaces.GameState;
import com.popovych.game.messages.*;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

public class DefaultServerGame implements Game.ServerGame {
    boolean gameRunning;

    protected GameMode gameMode;
    protected GameMessageTransmitter transmitter;

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
        gameMode.getGameState();
        gameMode.spawnActors();
        transmitter = args.getMessageTransmitter();
    }

    @Override
    public synchronized boolean isGameRunning() {
        return gameRunning;
    }

    @Override
    public synchronized void startGame() {
        gameRunning = true;
    }

    public synchronized void stopGame() {
        gameRunning = false;
    }

    @Override
    public void finaliseGame() {
        try {
            transmitter.broadcastMessage(new GameServerStoppedMessage());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameCycleIteration() {
        Message message = transmitter.receiveMessage();

        if (message.getType() == MessageType.GAME_CLIENT_SYNC) {
            GameClientSyncMessage syncMessage = (GameClientSyncMessage) message;
            if (syncMessage.getClientData().equals(gameMode.getGameState().getCurrentActor().getRegistered())) {
                gameMode.syncGameState(syncMessage.getStateToSync());
                try {
                    transmitter.broadcastMessage(new GameServerSyncMessage(gameMode.getGameState()));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else {
                transmitter.sendMessage(new GameServerActionDeniedMessage(syncMessage.getClientData()));
            }
        }
        else if (message.getType() == MessageType.GAME_CLIENT_REGISTER) {
            GameClientRegisterMessage registerMessage = (GameClientRegisterMessage) message;
            if (gameMode.getGameState().registerActor(registerMessage.getClientData())) {
                transmitter.sendMessage(new GameServerSyncMessage(registerMessage.getClientData(), gameMode.getGameState()));
            }
            else {
                transmitter.sendMessage(new GameServerActionDeniedMessage(registerMessage.getClientData()));
            }
        }
        else if (message.getType() == MessageType.GAME_CLIENT_WAIT) {
            GameClientWaitMessage waitMessage = (GameClientWaitMessage) message;
            if (waitMessage.getClientData() == gameMode.getGameState().getCurrentActor().getRegistered()) {
                transmitter.sendMessage(new GameServerSignalMessage(waitMessage.getClientData()));
            }
        }
        else if (message.getType() == MessageType.GAME_CLIENT_UNREGISTER) {
            GameClientUnregisterMessage unregisterMessage = (GameClientUnregisterMessage) message;
            gameMode.getGameState().unregisterActor(unregisterMessage.getClientData());
        }
    }
}
