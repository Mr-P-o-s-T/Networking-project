package com.popovych.networking.server.uncover;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.server.uncover.args.ServerUncoverArguments;
import com.popovych.networking.statics.Naming;

public class ServerUncoverThread extends ThreadGroupWorker {
    protected ServerUncoverThread(ThreadGroup group, ServerUncoverArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.serverUncoverThread, group);
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
