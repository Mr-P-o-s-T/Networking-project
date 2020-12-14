package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.enumerations.MessageType;

public class ServerDatabaseResponseMessage extends DefaultMessage {
    protected ServerDatabaseResponseData sdrData = null;

    public ServerDatabaseResponseMessage() {
        super(MessageType.NONE);
    }

    public ServerDatabaseResponseMessage(ServerDatabaseResponseData sdrData) {
        super(MessageType.DATABASE_SERVERS_RESPONSE);
        this.sdrData = sdrData;
    }

    public ServerDatabaseResponseData getSDRData() {
        return sdrData;
    }
}
