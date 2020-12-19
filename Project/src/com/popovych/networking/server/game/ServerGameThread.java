package com.popovych.networking.server.game;

import com.popovych.game.messages.DefaultGameMessage;
import com.popovych.game.interfaces.Game;
import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.game.args.ServerGameThreadArguments;
import com.popovych.networking.statics.Naming;

public class ServerGameThread extends ThreadGroupWorker implements MessageQueueProvider, GameMessageTransmitter {
    protected PlayerDataContainer playersGameData = new PlayerDataContainer();
    protected Game.ServerGame game;
    protected ServerGameArguments sArgs;

    protected int transmitterIndex = 0;

    public ServerGameThread(ThreadGroup group, ServerGameThreadArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.gameThread, group);
        game = args.getGameClass();
        sArgs = args.getGameArguments();
        sArgs.setGameMessageTransmitter(this);
    }

    @Override
    protected void prepareTask() {
        game.initGame(sArgs);
    }

    @Override
    protected void runTask() {
        game.startGame();
        while (game.isGameRunning()) {
            game.gameCycleIteration();
        }
        interrupt();
    }

    @Override
    protected void finishTask() {
        game.finaliseGame();
    }

    @Override
    public synchronized InputMessageQueue getInputMessageQueue() {
        return (InputMessageQueue) playersGameData.getOutputMessageQueue();
    }

    @Override
    public synchronized OutputMessageQueue getOutputMessageQueue() {
        return (OutputMessageQueue) playersGameData.getInputMessageQueue();
    }

    @Override
    public void sendMessage(Message message) {
        playersGameData.pickOutputMessageQueue((DefaultGameMessage) message).postMessage(message);
    }

    @Override
    public void broadcastMessage(Message message) {
        for (int i = 0; i < playersGameData.size(); i++) {
            playersGameData.pickOutputMessageQueue(i).postMessage(message);
        }
    }

    @Override
    public Message receiveMessage() {
        Message message = null;
        while (message == null) {
            message = playersGameData.pickInputMessageQueue(
                    transmitterIndex++).pollMessage();
            if (transmitterIndex >=  playersGameData.size()) {
                transmitterIndex = 0;
                Thread.yield();
            }
        }
        return message;
    }
}
