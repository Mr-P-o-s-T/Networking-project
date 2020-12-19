package com.popovych.networking.abstracts.messages;

import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;

import java.io.Serializable;

public abstract class DefaultMessage implements Serializable, Message {
    protected MessageType type;

    public DefaultMessage(MessageType type) {
        this.type = type;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
