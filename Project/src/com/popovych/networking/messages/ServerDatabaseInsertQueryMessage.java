package com.popovych.networking.messages;

import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class ServerDatabaseInsertQueryMessage extends ServerDatabaseQueryMessage {
    @Serial
    private static final long serialVersionUID = MessageType.SERVER_INSERT_QUERY.ordinal();

    protected ServerData sData = null;

    public ServerDatabaseInsertQueryMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseInsertQueryMessage(ServerData sData) {
        super(MessageType.SERVER_INSERT_QUERY);
        this.sData = sData;
    }

    public ServerData getServerData() {
        return sData;
    }

    public void setServerData(ServerData sData) {
        this.sData = sData;
    }
}
