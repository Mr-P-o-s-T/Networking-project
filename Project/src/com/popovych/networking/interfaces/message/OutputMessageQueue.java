package com.popovych.networking.interfaces.message;

public interface OutputMessageQueue {
    void postMessage(Message message) throws InterruptedException;
}
