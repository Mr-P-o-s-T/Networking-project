package com.popovych.networking.interfaces.threadgroup;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.interfaces.args.Arguments;

public interface WorkerSpawner {
    NetRunnable SpawnWorker(Arguments workerArgs) throws Exception;
}
