package com.popovych.networking.abstracts.threads;

import com.popovych.networking.abstracts.Indexer;

public abstract class NetRunnable implements Runnable {
    private final Thread executor;

    private static Indexer<Integer> indexer = null;

    NetRunnable(String template, String description) {
        this(template, description, null);
    }

    NetRunnable(String template, String description, ThreadGroup group) {
        executor = new Thread(group, this, String.format(template, getIndexer().getIndex(), description));
        executor.start();
    }

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
        return !executor.isInterrupted();
    }

    public void interrupt() {
        executor.interrupt();
    }

    public void join() {
        if (Thread.currentThread() != executor) {
            try {
                executor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected static synchronized Indexer<Integer> getIndexer() {
        if (indexer == null) {
            return indexer = new Indexer<>() {
                @Override
                protected void updateIndex() {
                    index++;
                }
            };
        }
        return indexer;
    }

}
