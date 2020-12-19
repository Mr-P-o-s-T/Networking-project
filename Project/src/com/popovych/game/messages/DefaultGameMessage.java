package com.popovych.game.messages;

import com.popovych.networking.abstracts.messages.DefaultMessage;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;

public class DefaultGameMessage extends DefaultMessage {
    protected ClientData cData;

    public DefaultGameMessage(MessageType type, ClientData cData) {
        super(type);
        this.cData = cData;
    }

    public ClientData getClientData() {
        return cData;
    }

    public void setClientData(ClientData cData) {
        this.cData = cData;
    }
}
