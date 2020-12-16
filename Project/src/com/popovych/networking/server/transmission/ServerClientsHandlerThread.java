package com.popovych.networking.server.transmission;

import com.popovych.networking.abstracts.threads.NetRunnable;
import com.popovych.networking.abstracts.threads.ThreadSubgroupMaster;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;
import com.popovych.networking.server.transmission.args.ServerClientTransmitterThreadArguments;
import com.popovych.networking.server.transmission.args.ServerClientsHandlerThreadArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientsHandlerThread extends ThreadSubgroupMaster {
    protected ServerData sData;
    protected ServerSocket socket;

    protected MessageQueueProvider provider;

    public ServerClientsHandlerThread(ThreadGroup group, ServerClientsHandlerThreadArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.clientsHandlerThread, group,
                Naming.Groups.clientHandler);
        sData = args.getSData();
        provider = args.getMessageQueueProvider();
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
        Socket client = null;
        try {
            client = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        try {
            SpawnWorker(new ServerClientTransmitterThreadArguments(client, provider.getInputMessageQueue(),
                    provider.getOutputMessageQueue()));
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
            return new ServerClientTransmitterThread(group, (ServerClientTransmitterThreadArguments) workerArgs);
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public void SpawnWorkerMemo(Arguments workerArgs) throws Exception {

    }
}
