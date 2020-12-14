package com.popovych.networking.server.transmission.args;

import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

import java.net.Socket;

public class ServerClientTransmitterArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_SOCKET(THREAD_TYPE);

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

    public ServerClientTransmitterArguments(Socket clientHandlerSocket) {
        super(ServerWorkerThreadType.CLIENT_TRANSMITTER);
        args.add(clientHandlerSocket);
    }

    public Socket getClientHandlerSocket() {
        return (Socket) args.get(ArgsType.CLIENT_SOCKET.getIndex());
    }
}
