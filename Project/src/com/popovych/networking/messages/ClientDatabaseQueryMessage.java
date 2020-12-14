package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

public class ClientDatabaseQueryMessage extends DefaultMessage {
    protected ClientData cData = null;

    public ClientDatabaseQueryMessage() {
        super(MessageType.NONE);
    }

    public ClientDatabaseQueryMessage(ClientData cData) {
        super(MessageType.CLIENT_SERVERS_QUERY);
        this.cData = cData;
    }

    public ClientData getCData() {
        return cData;
    }
}
