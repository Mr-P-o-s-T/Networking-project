package com.popovych.game.interfaces;

import com.popovych.networking.interfaces.message.Message;

public interface GameMessageTransmitter {
    void sendMessage(Message message) throws InterruptedException;
    void broadcastMessage(Message message) throws InterruptedException;

    Message receiveMessage() throws InterruptedException;
}
