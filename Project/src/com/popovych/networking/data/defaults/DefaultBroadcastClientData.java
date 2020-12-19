package com.popovych.networking.data.defaults;

import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;

public class DefaultBroadcastClientData extends DefaultClientData{
    public DefaultBroadcastClientData() throws UnknownHostException {
        super(Naming.Constants.broadcast, Naming.Constants.defaultBroadcastIP, Naming.Constants.defaultClientPort);
    }
}
