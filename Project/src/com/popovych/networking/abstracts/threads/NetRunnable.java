package com.popovych.networking.abstracts.threads;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.interfaces.args.Arguments;

public abstract class NetRunnable implements Runnable {
    private final Thread executor;
    private int oneIteration = -1;

    NetRunnable(Arguments args, String template, String description, Indexer<Integer> indexer, boolean isOneIteration,
                boolean launchExecutor) {
        this(args, template, description, indexer, null, isOneIteration, launchExecutor);
    }

    NetRunnable(Arguments args, String template, String description, Indexer<Integer> indexer, ThreadGroup group,
                boolean isOneIteration, boolean launchExecutor) {
        if (isOneIteration)
            this.oneIteration = -oneIteration;
        executor = new Thread(group, this, String.format(template, indexer.getIndex(), description));
        processArgs(args);
        if (launchExecutor)
            executor.start();
    }

    protected abstract void processArgs(Arguments arguments);

    @Override
    public void run() {
        prepareTask();
        while (isRunning()) {
            runTask();
        }
        finishTask();
    }

    protected abstract void prepareTask();
    protected abstract void runTask();
    protected abstract void finishTask();

    public boolean isRunning() {
        if (oneIteration >= 0) {
            if (executor.isInterrupted() || oneIteration == 0) return false;
            oneIteration--;
            return true;
        }
        else
            return !executor.isInterrupted();
    }

    public void interrupt() {
        executor.interrupt();
    }

    public void join() throws InterruptedException{
        if (Thread.currentThread() != executor) {
            executor.join();
        }
    }

}
