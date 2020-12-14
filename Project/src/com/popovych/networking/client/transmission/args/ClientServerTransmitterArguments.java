package com.popovych.networking.client.transmission.args;

import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.client.ClientWorkerThreadArguments;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.interfaces.ServerSearchController;

public class ClientServerTransmitterArguments extends ClientWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_DATA(THREAD_TYPE),
        SEARCH_CONTROLLER(CLIENT_DATA),
        DATA_PROVIDER(SEARCH_CONTROLLER),
        INPUT_MESSAGE_QUEUE(DATA_PROVIDER),
        OUTPUT_MESSAGE_QUEUE(INPUT_MESSAGE_QUEUE);

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
    public ClientServerTransmitterArguments(ClientData cData, ServerSearchController controller, ServerDataProvider provider,
                                            ClientMessageQueue inputMessageQueue, ClientMessageQueue outputMessageQueue) {
        super(ClientWorkerThreadType.SERVER_TRANSMITTER);
        args.add(cData);
        args.add(controller);
        args.add(provider);
        args.add(inputMessageQueue);
        args.add(outputMessageQueue);
    }

    public ClientData getCData() {
        return (ClientData) args.get(ArgsType.CLIENT_DATA.getIndex());
    }

    public ServerSearchController getSearchController() {
        return (ServerSearchController) args.get(ArgsType.SEARCH_CONTROLLER.getIndex());
    }

    public ServerDataProvider getProvider() {
        return (ServerDataProvider) args.get(ArgsType.DATA_PROVIDER.getIndex());
    }

    public ClientMessageQueue getInputMessageQueue() {
        return (ClientMessageQueue) args.get(ArgsType.INPUT_MESSAGE_QUEUE.getIndex());
    }

    public ClientMessageQueue getOutputMessageQueue() {
        return (ClientMessageQueue) args.get(ArgsType.OUTPUT_MESSAGE_QUEUE.getIndex());
    }
}
