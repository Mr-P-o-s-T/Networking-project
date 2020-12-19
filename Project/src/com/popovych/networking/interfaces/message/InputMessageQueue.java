package com.popovych.networking.interfaces.message;

public interface InputMessageQueue {
    boolean isMessagePresent();

    Message getMessage();
    Message pollMessage();
}
