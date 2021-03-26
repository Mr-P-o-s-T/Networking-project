package com.popovych.networking.abstracts.messages;

import com.popovych.game.messages.GameServerActionDeniedMessage;
import com.popovych.networking.data.defaults.DefaultClientData;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;

import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class DefaultMessageQueueImplementation implements InputMessageQueue, OutputMessageQueue {
    protected LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    @Override
    public void postMessage(Message message) throws InterruptedException {
            queue.put(message);
    }

    @Override
    public boolean isMessagePresent() {
        return queue.size() > 0;
    }

    @Override
    public Message getMessage() {
        return queue.peek();
    }

    @Override
    public Message pollMessage() throws InterruptedException{
        return queue.take();
    }
}
