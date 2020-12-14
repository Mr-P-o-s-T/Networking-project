package com.popovych.networking.interfaces;

import com.popovych.networking.interfaces.message.Message;

public interface MessageQueue {
    void postMessage(Message message);
    Message getMessage();
    Message pollMessage();
}
