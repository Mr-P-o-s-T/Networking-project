package com.popovych.networking.client.transmission.args;

import com.popovych.networking.client.ClientWorkerThreadArguments;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.ServerDataProvider;

public class ClientServerTransmitterThreadArguments extends ClientWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_DATA(THREAD_TYPE),
        SEARCH_CONTROLLER(CLIENT_DATA),
        DATA_PROVIDER(SEARCH_CONTROLLER),
        MESSAGE_QUEUE_PROVIDER(DATA_PROVIDER);

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
    public ClientServerTransmitterThreadArguments(ClientData cData, DatabaseController controller,
                                                  ServerDataProvider sDataProvider, MessageQueueProvider queueProvider) {
        super(ClientWorkerThreadType.SERVER_TRANSMITTER);
        args.add(cData);
        args.add(controller);
        args.add(sDataProvider);
        args.add(queueProvider);
    }

    public ClientData getCData() {
        return (ClientData) args.get(ArgsType.CLIENT_DATA.getIndex());
    }

    public DatabaseController getSearchController() {
        return (DatabaseController) args.get(ArgsType.SEARCH_CONTROLLER.getIndex());
    }

    public ServerDataProvider getServerDataProvider() {
        return (ServerDataProvider) args.get(ArgsType.DATA_PROVIDER.getIndex());
    }

    public MessageQueueProvider getMessageQueueProvider() {
        return (MessageQueueProvider) args.get(ArgsType.MESSAGE_QUEUE_PROVIDER.getIndex());
    }
}
