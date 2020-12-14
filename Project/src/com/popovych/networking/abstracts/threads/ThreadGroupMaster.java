package com.popovych.networking.abstracts.threads;

import com.popovych.networking.interfaces.threadgroup.WorkerSpawner;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawnerMemo;

public abstract class ThreadGroupMaster extends NetRunnable implements WorkerSpawner, WorkerSpawnerMemo {
    protected ThreadGroup group;

    protected ThreadGroupMaster(String template, String description, String groupName) {
        super(template, description);
        this.group = new ThreadGroup(groupName);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        group.interrupt();
    }
}
