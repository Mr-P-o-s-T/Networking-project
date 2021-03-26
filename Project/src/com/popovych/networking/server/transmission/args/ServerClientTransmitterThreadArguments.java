package com.popovych.networking.server.transmission.args;

import com.popovych.game.interfaces.ClientDataContainer;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;
import com.popovych.networking.server.message.ServerMessageQueue;

import java.net.Socket;

public class ServerClientTransmitterThreadArguments extends ServerWorkerThreadArguments {


    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_SOCKET(THREAD_TYPE),
        MESSAGE_QUEUE_PROVIDER(CLIENT_SOCKET),
        CLIENT_DATA_CONTAINER(MESSAGE_QUEUE_PROVIDER);

        private final int index;
        private final int autoinc;

        ArgsType(ArgsType prevType) {
            this(prevType.index + prevType.autoinc, prevType.autoinc);
        }

        ArgsType(int i) {
            this(i, 1);
        }

        ArgsType(int i, int autoinc) {
            index = i;
            this.autoinc = autoinc;
        }

        public int getIndex() {
            return index;
        }
    }

    public ServerClientTransmitterThreadArguments(Socket clientHandlerSocket, MessageQueueProvider provider,
                                                  ClientDataContainer container) {
        super(ServerWorkerThreadType.CLIENT_TRANSMITTER);
        args.add(clientHandlerSocket);
        args.add(provider);
        args.add(container);
    }

    public Socket getClientHandlerSocket() {
        return (Socket) args.get(ArgsType.CLIENT_SOCKET.getIndex());
    }

    public MessageQueueProvider getMessageQueueProvider() {
        return (MessageQueueProvider) args.get(ArgsType.MESSAGE_QUEUE_PROVIDER.getIndex());
    }

    public ClientDataContainer getClientDataContainer() {
        return (ClientDataContainer) args.get(ArgsType.CLIENT_DATA_CONTAINER.getIndex());
    }
}
