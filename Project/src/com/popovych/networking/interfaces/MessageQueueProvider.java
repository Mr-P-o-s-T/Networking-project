package com.popovych.networking.interfaces;

import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

public interface MessageQueueProvider {
    InputMessageQueue getInputMessageQueue();
    OutputMessageQueue getOutputMessageQueue();
}
