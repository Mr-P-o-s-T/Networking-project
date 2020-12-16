package com.popovych.networking.interfaces.message;

public interface InputMessageQueue {
    Message getMessage();
    Message pollMessage();
}
