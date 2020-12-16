package com.popovych.networking.server.transmission.args;

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
        INPUT_MESSAGE_QUEUE(CLIENT_SOCKET),
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

    public ServerClientTransmitterThreadArguments(Socket clientHandlerSocket, InputMessageQueue inputMessageQueue,
                                                  OutputMessageQueue outputMessageQueue) {
        super(ServerWorkerThreadType.CLIENT_TRANSMITTER);
        args.add(clientHandlerSocket);
        args.add(inputMessageQueue);
        args.add(outputMessageQueue);
    }

    public Socket getClientHandlerSocket() {
        return (Socket) args.get(ArgsType.CLIENT_SOCKET.getIndex());
    }

    public ServerMessageQueue getInputMessageQueue() {
        return (ServerMessageQueue) args.get(ArgsType.INPUT_MESSAGE_QUEUE.getIndex());
    }

    public ServerMessageQueue getOutputMessageQueue() {
        return (ServerMessageQueue) args.get(ArgsType.OUTPUT_MESSAGE_QUEUE.getIndex());
    }
}
