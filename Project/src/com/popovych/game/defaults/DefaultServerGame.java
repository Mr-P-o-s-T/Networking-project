package com.popovych.game.defaults;

import com.popovych.game.args.GameModeArguments;
import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.*;
import com.popovych.game.messages.*;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.defaults.DefaultBroadcastClientData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.statics.Naming;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.List;

public class DefaultServerGame implements Game.ServerGame {
    boolean gameRunning, serverIsFull = false;
    int winState = 0;

    protected GameMode gameMode;
    protected GameMessageTransmitter transmitter;

    protected ServerData sData;

    @Override
    public void initGame(ServerGameArguments args) {
        sData = args.getServerData();
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
        } catch (InterruptedException ignored) {

        }
    }

    void checkWinState() {
        char res = gameMode.getGameState().checkWinState();
        if (res != 0) {
            if (res == Naming.Constants.Stalemate) {
                try {
                    transmitter.broadcastMessage(new GameClientStalemateMessage(new DefaultBroadcastClientData()));
                } catch (InterruptedException e) {
                    stopGame();
                    Thread.currentThread().interrupt();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else {
                Actor winner, loser;
                List<Actor> actors = gameMode.getGameState().getActors();
                if (((DefaultActor) actors.get(0)).getMark() == res) {
                    winner = actors.get(0);
                    loser = actors.get(1);
                } else if (((DefaultActor) actors.get(1)).getMark() == res) {
                    winner = actors.get(1);
                    loser = actors.get(0);
                } else {
                    return;
                }
                try {
                    transmitter.sendMessage(new GameClientWinMessage(winner.getRegistered()));
                    if (loser.getRegistered() != null)
                        transmitter.sendMessage(new GameClientLoseMessage(loser.getRegistered()));
                } catch (InterruptedException e) {
                    stopGame();
                    Thread.currentThread().interrupt();
                }
            }
            winState = gameMode.getGameState().getActors().size();
        }
    }

    @Override
    public void gameCycleIteration() {
        Message message;
        try {
            message = transmitter.receiveMessage();
        } catch (InterruptedException e) {
            stopGame();
            Thread.currentThread().interrupt();
            return;
        }

        if (serverIsFull) {
            if (message.getType() == MessageType.GAME_CLIENT_SYNC) {
                GameClientSyncMessage syncMessage = (GameClientSyncMessage) message;
                if (syncMessage.getClientData().equals(gameMode.getGameState().getCurrentActor().getRegistered())) {
                    gameMode.syncGameState(syncMessage.getStateToSync());
                    try {
                        transmitter.broadcastMessage(new GameServerSyncMessage(gameMode.getGameState()));
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        stopGame();
                        Thread.currentThread().interrupt();
                    }
                    checkWinState();
                } else {
                    try {
                        transmitter.sendMessage(new GameServerActionDeniedMessage(syncMessage.getClientData()));
                    } catch (InterruptedException e) {
                        stopGame();
                        Thread.currentThread().interrupt();
                    }
                }
            } else if (message.getType() == MessageType.GAME_CLIENT_WAIT) {
                GameClientWaitMessage waitMessage = (GameClientWaitMessage) message;
                if (winState == 0) {
                    if (waitMessage.getClientData().equals(gameMode.getGameState().getCurrentActor().getRegistered())) {
                        try {
                            transmitter.sendMessage(new GameServerSignalMessage(waitMessage.getClientData()));
                        } catch (InterruptedException e) {
                            stopGame();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } else if (message.getType() == MessageType.GAME_SERVER_RESTART) {
                GameServerRestart restartMessage = (GameServerRestart) message;
                if (--winState == 0) {
                    gameMode.getGameState().resetScene();
                    try {
                        transmitter.broadcastMessage(new GameServerSyncMessage(restartMessage.getClientData(),
                                gameMode.getGameState()));
                    } catch (InterruptedException e) {
                        stopGame();
                        Thread.currentThread().interrupt();
                    }
                }
            } else if (message.getType() == MessageType.GAME_CLIENT_UNREGISTER) {
                GameClientUnregisterMessage unregisterMessage = (GameClientUnregisterMessage) message;
                gameMode.getGameState().unregisterActor(unregisterMessage.getClientData());
                gameMode.getGameState().resetScene();
                serverIsFull = false;
                if (winState > 0)
                    winState--;
            }
        } else {
            if (gameMode.getGameState().canRegister()) {
                if (message.getType() == MessageType.GAME_CLIENT_REGISTER) {
                    GameClientRegisterMessage registerMessage = (GameClientRegisterMessage) message;
                    if (registerMessage.getClientData().getLoggingPassword().equals(sData.getPassword()) &&
                            gameMode.getGameState().registerActor(registerMessage.getClientData())) {
                        try {
                            transmitter.sendMessage(new GameServerActionAcceptedMessage(
                                    registerMessage.getClientData()));
                        } catch (InterruptedException e) {
                            stopGame();
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        try {
                            transmitter.sendMessage(new GameServerActionDeniedMessage(registerMessage.getClientData()));
                        } catch (InterruptedException e) {
                            stopGame();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } else {
                serverIsFull = true;
                try {
                    transmitter.broadcastMessage(new GameServerSyncMessage(gameMode.getGameState()));
                } catch (InterruptedException e) {
                    stopGame();
                    Thread.currentThread().interrupt();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    stopGame();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
