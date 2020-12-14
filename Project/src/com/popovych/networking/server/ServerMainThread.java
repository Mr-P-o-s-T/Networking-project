package com.popovych.networking.server;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadGroupMaster;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.statics.Naming;

public class ServerMainThread extends ThreadGroupMaster {
    protected ServerMainThread() {
        super(Naming.Templates.serverThread, Naming.Descriptions.mainThread, Naming.Groups.server);
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

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        return null;
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
