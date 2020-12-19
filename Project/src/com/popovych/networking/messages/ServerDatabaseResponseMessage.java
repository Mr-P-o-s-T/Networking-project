package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class ServerDatabaseResponseMessage extends DefaultMessage {
    @Serial
    private static final long serialVersionUID = MessageType.DATABASE_SERVERS_RESPONSE.ordinal();

    protected ServerDatabaseResponseData sdrData = null;

    public ServerDatabaseResponseMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseResponseMessage(ServerDatabaseResponseData sdrData) {
        super(MessageType.DATABASE_SERVERS_RESPONSE);
        this.sdrData = sdrData;
    }

    public ServerDatabaseResponseData getServerDatabaseResponseData() {
        return sdrData;
    }

    public void setServerDatabaseResponseData(ServerDatabaseResponseData sdrData) {
        this.sdrData = sdrData;
    }
}
