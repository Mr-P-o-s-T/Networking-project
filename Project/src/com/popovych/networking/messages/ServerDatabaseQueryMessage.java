package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.enumerations.MessageType;

public class ServerDatabaseQueryMessage extends DefaultMessage {
    public ServerDatabaseQueryMessage(MessageType type) {
        super(type);
    }
}
