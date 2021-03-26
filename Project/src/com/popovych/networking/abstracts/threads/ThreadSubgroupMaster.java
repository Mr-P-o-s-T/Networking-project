package com.popovych.networking.abstracts.threads;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawner;
import com.popovych.networking.interfaces.threadgroup.WorkerSpawnerMemo;

public abstract class ThreadSubgroupMaster extends NetRunnable implements WorkerSpawner, WorkerSpawnerMemo {
    protected ThreadGroup group;

    protected ThreadSubgroupMaster(Arguments args, String template, String description, ThreadGroup group,
                                   String subgroupName, Indexer<Integer> indexer, boolean isOneIteration) {
        super(args, template, description, indexer, group, isOneIteration, true);
        this.group = new ThreadGroup(group, subgroupName);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        group.interrupt();
    }

    @Override
    public void join() throws InterruptedException{
        Thread[] threads = new Thread[group.activeCount()];
        group.enumerate(threads);

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        super.join();
    }
}
