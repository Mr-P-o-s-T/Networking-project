package com.popovych.game.interfaces;

import com.popovych.networking.interfaces.message.Message;

public interface GameMessageTransmitter {
    void sendMessage(Message message);
    void broadcastMessage(Message message);

    Message receiveMessage();
}
