package com.popovych.networking.abstracts.data;

import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

public abstract class GameTransmitterGameData {
    protected final OutputMessageQueue outputMessageQueue;

    public GameTransmitterGameData(OutputMessageQueue outputMessageQueue) {
        this.outputMessageQueue = outputMessageQueue;
    }

    public OutputMessageQueue getOutputMessageQueue() {
        synchronized (outputMessageQueue) {
            return outputMessageQueue;
        }
    }
}
