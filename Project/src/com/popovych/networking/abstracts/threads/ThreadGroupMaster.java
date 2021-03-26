package com.popovych.networking.abstracts.threads;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawner;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawnerMemo;

import java.util.Arrays;

public abstract class ThreadGroupMaster extends NetRunnable implements WorkerSpawner, WorkerSpawnerMemo {
    protected ThreadGroup group;

    protected ThreadGroupMaster(Arguments args, String template, String description, String groupName,
                                Indexer<Integer> indexer, boolean isOneIteration, boolean launchExecutor) {
        super(args, template, description, indexer, isOneIteration, launchExecutor);
        this.group = new ThreadGroup(groupName);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        group.interrupt();
    }

    @Override
    protected void runTask() {
        try {
            join();
        } catch (InterruptedException e) {
            interrupt();
            return;
        }
        Thread.yield();
    }

    @Override
    public void join() throws InterruptedException {
        Thread[] threads = new Thread[group.activeCount()];
        group.enumerate(threads);

        for (Thread thread : threads) {
            if (thread != null)
                thread.join();
        }

        super.join();
    }
}
