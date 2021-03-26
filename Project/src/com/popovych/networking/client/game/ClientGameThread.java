package com.popovych.networking.client.game;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.game.interfaces.GameMessageTransmitter;
import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.game.args.ClientGameThreadArguments;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.statics.Naming;

public class ClientGameThread extends ThreadGroupWorker implements MessageQueueProvider, GameMessageTransmitter {
    protected ServerGameData data;
    protected Game.ClientGame game;
    protected ClientGameArguments cArgs;

    public ClientGameThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.clientThread, Naming.Descriptions.gameThread, group, indexer, true);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        data = new ServerGameData();
        ClientGameThreadArguments args = (ClientGameThreadArguments) arguments;
        game = args.getGameClass();
        cArgs = args.getGameArguments();
        cArgs.setGameMessageTransmitter(this);
    }

    @Override
    protected void prepareTask() {
        game.initGame(cArgs);
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
    public InputMessageQueue getInputMessageQueue() {
        while (data.getOutputMessageQueue() == null)
            Thread.yield();
        return (InputMessageQueue) data.getOutputMessageQueue();
    }

    @Override
    public OutputMessageQueue getOutputMessageQueue() {
        while (data.getInputMessageQueue() == null)
            Thread.yield();
        return (OutputMessageQueue) data.getInputMessageQueue();
    }

    @Override
    public void sendMessage(Message message) throws InterruptedException {
        data.getOutputMessageQueue().postMessage(message);
    }

    @Override
    public void broadcastMessage(Message message) throws InterruptedException {
        sendMessage(message);
    }

    @Override
    public Message receiveMessage() throws InterruptedException{
        return data.getInputMessageQueue().pollMessage();
    }
}
