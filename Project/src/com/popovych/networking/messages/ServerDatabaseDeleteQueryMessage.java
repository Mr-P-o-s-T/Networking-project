package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class ServerDatabaseDeleteQueryMessage extends ServerDatabaseQueryMessage {
    @Serial
    private static final long serialVersionUID = MessageType.SERVER_DELETE_QUERY.ordinal();

    protected ServerData sData = null;

    public ServerDatabaseDeleteQueryMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseDeleteQueryMessage(ServerData sData) {
        super(MessageType.SERVER_DELETE_QUERY);
        this.sData = sData;
    }

    public ServerData getServerData() {
        return sData;
    }

    public void setServerData(ServerData sData) {
        this.sData = sData;
    }
}
