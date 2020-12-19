package com.popovych.networking.server.game;

import com.popovych.game.messages.DefaultGameMessage;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataContainer implements MessageQueueProvider {
    protected List<PlayerGameData> playersGameData = new ArrayList<>();

    protected PlayerGameData getRegisteredThreadData() {
        for (PlayerGameData pgData: playersGameData) {
            if (pgData.isRegisteredThread()) {
                return pgData;
            }
        }
        PlayerGameData pgData = new PlayerGameData(Thread.currentThread());
        playersGameData.add(pgData);
        return pgData;
    }

    public InputMessageQueue pickInputMessageQueue(int index) {
        if (playersGameData.get(index).getInputMessageQueue().isMessagePresent()) {
            return playersGameData.get(index).getInputMessageQueue();
        }
        return null;
    }

    public OutputMessageQueue pickOutputMessageQueue(int index) {
        return playersGameData.get(index).getOutputMessageQueue();
    }

    public OutputMessageQueue pickOutputMessageQueue(DefaultGameMessage message) {
        for (PlayerGameData pgData: playersGameData) {
            if (pgData.getClientData().equals(message.getClientData())) {
                return pgData.getOutputMessageQueue();
            }
        }
        return null;
    }

    @Override
    public InputMessageQueue getInputMessageQueue() {
        return getRegisteredThreadData().getInputMessageQueue();
    }

    @Override
    public OutputMessageQueue getOutputMessageQueue() {
        return getRegisteredThreadData().getOutputMessageQueue();
    }

    public int size() {
        return playersGameData.size();
    }
}
