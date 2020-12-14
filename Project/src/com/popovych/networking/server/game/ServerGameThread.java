package com.popovych.networking.server.game;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.game.args.ClientGameArguments;
import com.popovych.networking.server.game.args.ServerGameArguments;
import com.popovych.networking.statics.Naming;

public class ServerGameThread extends ThreadGroupWorker {
    public ServerGameThread(ThreadGroup group, ServerGameArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.gameThread, group);
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {

    }

    @Override
    protected void finishTask() {

    }
}
