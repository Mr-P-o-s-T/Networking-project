package com.popovych.networking.server.transmission.args;

import com.popovych.game.interfaces.ClientDataContainer;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerClientsHandlerThreadArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATA(THREAD_TYPE),
        MESSAGE_QUEUE_PROVIDER(SERVER_DATA),
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

    public ServerClientsHandlerThreadArguments(ServerData sData, MessageQueueProvider provider,
                                               ClientDataContainer container) {
        super(ServerWorkerThreadType.CLIENTS_HANDLER);
        args.add(sData);
        args.add(provider);
        args.add(container);
    }

    public ServerData getSData() {
        return (ServerData) args.get(ArgsType.SERVER_DATA.getIndex());
    }

    public MessageQueueProvider getMessageQueueProvider() {
        return (MessageQueueProvider) args.get(ArgsType.MESSAGE_QUEUE_PROVIDER.getIndex());
    }

    public ClientDataContainer getClientDataContainer() {
        return (ClientDataContainer) args.get(ArgsType.CLIENT_DATA_CONTAINER.getIndex());
    }
}
