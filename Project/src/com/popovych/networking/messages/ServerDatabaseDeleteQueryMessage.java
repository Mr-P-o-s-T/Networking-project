package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;

public class ServerDatabaseDeleteQueryMessage extends ServerDatabaseQueryMessage {
    protected ServerData sData = null;

    public ServerDatabaseDeleteQueryMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseDeleteQueryMessage(ServerData sData) {
        super(MessageType.SERVER_DELETE_QUERY);
        this.sData = sData;
    }

    public ServerData getSData() {
        return sData;
    }
}
