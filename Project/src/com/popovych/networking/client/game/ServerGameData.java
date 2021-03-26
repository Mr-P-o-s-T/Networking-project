package com.popovych.networking.client.game;

import com.popovych.networking.abstracts.data.GameTransmitterGameData;
import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

public class ServerGameData extends GameTransmitterGameData {
    protected InputMessageQueue inputMessageQueue;

    public ServerGameData() {
        this(new ClientMessageQueue(), new ClientMessageQueue());
    }

    protected ServerGameData(InputMessageQueue inputMessageQueue, OutputMessageQueue outputMessageQueue) {
        super(outputMessageQueue);
        this.inputMessageQueue = inputMessageQueue;
    }

    public InputMessageQueue getInputMessageQueue() {
        synchronized (inputMessageQueue) {
            return inputMessageQueue;
        }
    }
}
