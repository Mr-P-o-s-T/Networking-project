package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;

public class ServerDatabaseInsertQueryMessage extends ServerDatabaseQueryMessage {
    protected ServerData sData = null;

    public ServerDatabaseInsertQueryMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseInsertQueryMessage(ServerData sData) {
        super(MessageType.SERVER_INSERT_QUERY);
        this.sData = sData;
    }

    public ServerData getSData() {
        return sData;
    }
}
