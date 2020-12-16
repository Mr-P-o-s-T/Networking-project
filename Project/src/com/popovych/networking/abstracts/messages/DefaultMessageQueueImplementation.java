package com.popovych.networking.abstracts.messages;

import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

import java.util.concurrent.LinkedBlockingQueue;

public class DefaultMessageQueueImplementation implements InputMessageQueue, OutputMessageQueue {
    protected LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    @Override
    public void postMessage(Message message) {
        queue.add(message);
    }

    @Override
    public Message getMessage() {
        return queue.peek();
    }

    @Override
    public Message pollMessage() {
        return queue.poll();
    }
}
