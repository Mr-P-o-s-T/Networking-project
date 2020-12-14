package com.popovych.networking.server.transmission;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadSubgroupMaster;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;
import com.popovych.networking.server.transmission.args.ServerClientTransmitterArguments;
import com.popovych.networking.server.transmission.args.ServerClientsHandlerArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientsHandler extends ThreadSubgroupMaster {
    protected ServerData sData;
    protected ServerSocket socket;

    protected ServerClientsHandler(ThreadGroup group, ServerClientsHandlerArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.clientsHandlerThread, group,
                Naming.Groups.clientHandler);
        sData = args.getSData();
    }

    @Override
    protected void prepareTask() {
        try {
            socket = new ServerSocket(sData.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    @Override
    protected void runTask() {
        Socket server = null;
        try {
            server = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        try {
            SpawnWorker(new ServerClientTransmitterArguments(server));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finishTask() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public NetRunnable SpawnWorker(Arguments workerArgs) throws Exception {
        ServerWorkerThreadType newThreadType = ((ServerWorkerThreadArguments)workerArgs).getWorkerType();

        if (newThreadType == ServerWorkerThreadType.CLIENT_TRANSMITTER) {
            return new ServerClientTransmitter(group, (ServerClientTransmitterArguments) workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
