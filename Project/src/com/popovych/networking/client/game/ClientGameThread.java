package com.popovych.networking.client.game;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.game.args.ClientGameArguments;
import com.popovych.networking.statics.Naming;

public class ClientGameThread extends ThreadGroupWorker {
    public ClientGameThread(ThreadGroup group, ClientGameArguments args) {
        super(Naming.Templates.clientThread, Naming.Descriptions.gameThread, group);
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
