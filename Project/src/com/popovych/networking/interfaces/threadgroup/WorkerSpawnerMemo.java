package com.popovych.networking.interfaces.threadgroup;

import com.popovych.networking.interfaces.args.Arguments;

public interface WorkerSpawnerMemo {
    void SpawnWorkerMemo(Arguments workerArgs) throws Exception;
}
