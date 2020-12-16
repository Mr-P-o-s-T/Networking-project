package com.popovych.networking.abstracts.data;

import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

public abstract class GameTransmitterGameData {
    protected final InputMessageQueue inputMessageQueue;
    protected final OutputMessageQueue outputMessageQueue;

    public GameTransmitterGameData(InputMessageQueue inputMessageQueue, OutputMessageQueue outputMessageQueue) {
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
    }

    public InputMessageQueue getInputMessageQueue() {
        synchronized (inputMessageQueue) {
            return inputMessageQueue;
        }
    }

    public OutputMessageQueue getOutputMessageQueue() {
        synchronized (outputMessageQueue) {
            return outputMessageQueue;
        }
    }
}
