package com.popovych.networking.abstracts.threads;

public abstract class ThreadGroupWorker extends NetRunnable{
    protected ThreadGroupWorker(String template, String description, ThreadGroup group) {
        super(template, description, group);
    }
}
