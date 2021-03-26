package com.popovych.networking.abstracts.threads;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.interfaces.args.Arguments;

public abstract class ThreadGroupWorker extends NetRunnable{
    protected ThreadGroupWorker(Arguments args, String template, String description, ThreadGroup group,
                                Indexer<Integer> indexer, boolean isOneIteration) {
        super(args, template, description, indexer, group, isOneIteration, true);
    }
}
