package com.popovych.networking.server.game;

import com.popovych.networking.abstracts.data.GameTransmitterGameData;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.message.ServerMessageQueue;

public class PlayerGameData extends GameTransmitterGameData {
    protected ClientData cData;
    protected Thread registered;

    public PlayerGameData(Thread registered) {
        super(new ServerMessageQueue(), new ServerMessageQueue());
        this.registered = registered;
    }

    public PlayerGameData(InputMessageQueue inputMessageQueue, OutputMessageQueue outputMessageQueue) {
        super(inputMessageQueue, outputMessageQueue);
    }

    public void setClientData(ClientData cData) {
        this.cData = cData;
    }

    public ClientData getClientData() {
        return cData;
    }

    public boolean isRegisteredThread() {
        return Thread.currentThread().equals(registered);
    }
}
