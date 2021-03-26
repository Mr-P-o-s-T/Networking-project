package com.popovych.networking.server.game;

import com.popovych.game.interfaces.ClientDataContainer;
import com.popovych.game.messages.DefaultGameMessage;
import com.popovych.game.interfaces.Game;
import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.game.args.ServerGameThreadArguments;
import com.popovych.networking.server.message.ServerMessageQueue;
import com.popovych.statics.Naming;

public class ServerGameThread extends ThreadGroupWorker implements ClientDataContainer, MessageQueueProvider, GameMessageTransmitter {
    protected PlayerDataContainer playersGameData = new PlayerDataContainer();
    protected Game.ServerGame game;
    protected ServerGameArguments sArgs;
    protected InputMessageQueue inputMessageQueue = new ServerMessageQueue();

    protected int transmitterIndex = 0;

    public ServerGameThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.serverThread, Naming.Descriptions.gameThread, group, indexer, true);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ServerGameThreadArguments args = (ServerGameThreadArguments) arguments;
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
        return (OutputMessageQueue) inputMessageQueue;
    }

    @Override
    public void sendMessage(Message message) throws InterruptedException {
        playersGameData.pickOutputMessageQueue((DefaultGameMessage) message).postMessage(message);
    }

    @Override
    public void broadcastMessage(Message message) throws InterruptedException {
        for (int i = 0; i < playersGameData.size(); i++) {
            playersGameData.pickOutputMessageQueue(i).postMessage(message);
        }
    }

    @Override
    public Message receiveMessage() throws InterruptedException {
        while (playersGameData == null)
            Thread.yield();
        while (transmitterIndex >= playersGameData.size()) {
            transmitterIndex = 0;
            Thread.yield();
        }
        return inputMessageQueue.pollMessage();
    }

    @Override
    public void addNewClientData(ClientData cData) {
        playersGameData.addNewClientData(cData);
    }
}
