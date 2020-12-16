package com.popovych.networking.client.game;

import com.popovych.networking.abstracts.data.GameTransmitterGameData;
import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

public class ServerGameData extends GameTransmitterGameData {

    public ServerGameData() {
        super(new ClientMessageQueue(), new ClientMessageQueue());
    }

    public ServerGameData(InputMessageQueue inputMessageQueue, OutputMessageQueue outputMessageQueue) {
        super(inputMessageQueue, outputMessageQueue);
    }
}
