package com.popovych.networking.server.transmission;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.server.message.ServerMessageQueue;
import com.popovych.networking.server.transmission.args.ServerClientTransmitterArguments;
import com.popovych.networking.statics.Naming;

import java.net.Socket;

public class ServerClientTransmitter extends ThreadGroupWorker {
    protected ServerData sData;
    protected ServerMessageQueue inputMessageQueue;
    protected ServerMessageQueue outputMessageQueue;


    protected Socket clientHandlerSocket;
    protected ServerClientTransmitter(ThreadGroup group, ServerClientTransmitterArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.clientTransmitterThread, group);
        clientHandlerSocket = args.getClientHandlerSocket();
    }

    @Override
    protected void prepareTask() {

    }

    @Override
    protected void runTask() {

    }

    @Override
    protected void finishTask() {

    }
}
