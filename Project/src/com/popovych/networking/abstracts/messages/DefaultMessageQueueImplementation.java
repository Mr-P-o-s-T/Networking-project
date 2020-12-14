package com.popovych.networking.abstracts.messages;

import com.popovych.networking.interfaces.MessageQueue;
import com.popovych.networking.interfaces.message.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class DefaultMessageQueueImplementation implements MessageQueue {
    protected LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public void postMessage(Message message) {
        queue.add(message);
    }

    public Message getMessage() {
        return queue.peek();
    }

    public Message pollMessage() {
        return queue.poll();
    }
}
