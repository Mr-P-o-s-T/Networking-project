package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;

public class ServerDatabaseQueryMessage extends DefaultMessage {
    protected ServerData sData = null;

    public ServerDatabaseQueryMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseQueryMessage(ServerData sData) {
        super(MessageType.SERVER_INSERT_QUERY);
        this.sData = sData;
    }

    public ServerData getSData() {
        return sData;
    }
}
