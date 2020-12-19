package com.popovych.networking.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

import java.io.Serial;

public class ClientDatabaseQueryMessage extends DefaultMessage {
    @Serial
    private static final long serialVersionUID = MessageType.CLIENT_SERVERS_QUERY.ordinal();

    protected ClientData cData = null;

    public ClientDatabaseQueryMessage() {
        super(MessageType.NONE);
    }

    public ClientDatabaseQueryMessage(ClientData cData) {
        super(MessageType.CLIENT_SERVERS_QUERY);
        this.cData = cData;
    }

    public ClientData getClientData() {
        return cData;
    }

    public void setClientData(ClientData cData) {
        this.cData = cData;
    }
}
