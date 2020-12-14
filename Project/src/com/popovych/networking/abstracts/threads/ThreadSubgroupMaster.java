package com.popovych.networking.abstracts.threads;

import com.popovych.networking.interfaces.threadgroup.WorkerSpawner;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawnerMemo;

public abstract class ThreadSubgroupMaster extends NetRunnable implements WorkerSpawner, WorkerSpawnerMemo {
    protected ThreadGroup group;

    protected ThreadSubgroupMaster(String template, String description, ThreadGroup group, String subgroupName) {
        super(template, description, group);
        this.group = new ThreadGroup(group, subgroupName);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        group.interrupt();
    }
}
