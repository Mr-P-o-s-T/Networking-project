package com.popovych.networking.client.game;

import com.popovych.game.ClientGameArguments;
import com.popovych.game.Game;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.game.args.ClientGameThreadArguments;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.statics.Naming;

public class ClientGameThread extends ThreadGroupWorker implements MessageQueueProvider {
    protected ServerGameData data = new ServerGameData();
    protected Game.ClientGame game;
    protected ClientGameArguments cArgs;

    public ClientGameThread(ThreadGroup group, ClientGameThreadArguments args) {
        super(Naming.Templates.clientThread, Naming.Descriptions.gameThread, group);
        game = args.getGameClass();
        cArgs = args.getGameArguments();
    }

    @Override
    protected void prepareTask() {
        game.initGame(cArgs);
    }

    @Override
    protected void runTask() {
        game.startGame();
        interrupt();
    }

    @Override
    protected void finishTask() {
        game.finaliseGame();
    }

    @Override
    public InputMessageQueue getInputMessageQueue() {
        while (data.getOutputMessageQueue() == null);
        return (InputMessageQueue) data.getOutputMessageQueue();
    }

    @Override
    public OutputMessageQueue getOutputMessageQueue() {
        while (data.getInputMessageQueue() == null);
        return (OutputMessageQueue) data.getInputMessageQueue();
    }
}
