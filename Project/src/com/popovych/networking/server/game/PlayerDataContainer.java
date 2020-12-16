package com.popovych.networking.server.game;

import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataContainer implements MessageQueueProvider {
    protected List<PlayerGameData> playersGameData = new ArrayList<>();

    protected PlayerGameData getRegisteredThreadData() {
        for (PlayerGameData pgData : playersGameData) {
            if (pgData.isRegisteredThread()) {
                return pgData;
            }
        }
        PlayerGameData pgData = new PlayerGameData(Thread.currentThread());
        playersGameData.add(pgData);
        return pgData;
    }

    @Override
    public InputMessageQueue getInputMessageQueue() {
        return getRegisteredThreadData().getInputMessageQueue();
    }

    @Override
    public OutputMessageQueue getOutputMessageQueue() {
        return getRegisteredThreadData().getOutputMessageQueue();
    }
}
