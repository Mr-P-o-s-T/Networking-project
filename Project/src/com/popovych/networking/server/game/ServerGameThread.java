package com.popovych.networking.server.game;

import com.popovych.game.Game;
import com.popovych.game.ServerGameArguments;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.game.args.ServerGameThreadArguments;
import com.popovych.networking.statics.Naming;

public class ServerGameThread extends ThreadGroupWorker implements MessageQueueProvider {
    protected PlayerDataContainer playersGameData = new PlayerDataContainer();
    protected Game.ServerGame game;
    protected ServerGameArguments sArgs;

    public ServerGameThread(ThreadGroup group, ServerGameThreadArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.gameThread, group);
        game = args.getGameClass();
        sArgs = args.getGameArguments();
    }

    @Override
    protected void prepareTask() {
        game.initGame(sArgs);
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
    public synchronized InputMessageQueue getInputMessageQueue() {
        return (InputMessageQueue) playersGameData.getOutputMessageQueue();
    }

    @Override
    public synchronized OutputMessageQueue getOutputMessageQueue() {
        return (OutputMessageQueue) playersGameData.getInputMessageQueue();
    }
}
