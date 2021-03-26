package com.popovych.networking.client.transmission;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.transmission.args.ClientServerTransmitterThreadArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientServerTransmitterThread extends ThreadGroupWorker {
    protected ClientData cData;
    protected DatabaseController controller;
    protected ServerDataProvider sDataProvider;
    protected InputMessageQueue inputMessageQueue;
    protected OutputMessageQueue outputMessageQueue;

    protected Socket socket;

    public ClientServerTransmitterThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.clientThread, Naming.Descriptions.serverTransmitterThread, group, indexer,
                false);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ClientServerTransmitterThreadArguments args = (ClientServerTransmitterThreadArguments) arguments;
        cData = args.getCData();
        controller = args.getSearchController();
        sDataProvider = args.getServerDataProvider();

        MessageQueueProvider queueProvider = args.getMessageQueueProvider();
        inputMessageQueue = queueProvider.getInputMessageQueue();
        outputMessageQueue = queueProvider.getOutputMessageQueue();
    }

    @Override
    protected void prepareTask() {
        if (controller != null) {
            controller.getDatabaseControllerLocker().lock();
            try {
                if (controller.databaseActionExecutionNow())
                    controller.getDatabaseActionCompleteCondition().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            } finally {
                controller.getDatabaseControllerLocker().unlock();
            }
        }

        sDataProvider.getChosenServerLocker().lock();
        try {
            if (!sDataProvider.isServerChosen())
                sDataProvider.getChosenServerCondition().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            interrupt();
        } finally {
            sDataProvider.getChosenServerLocker().unlock();
        }

        try {
            socket = new Socket(sDataProvider.getServerData().getAddress(), sDataProvider.getServerData().getPort());
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    protected void sendMessage(Message message) throws IOException {
        ObjectOutputStream out = null;
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(message);
    }

    protected Message receiveMessage() throws IOException {
        Message message = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            message = (Message) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }
        return message;
    }

    @Override
    protected void runTask() {
        Message message = null;
        try {
            message = inputMessageQueue.pollMessage();
        } catch (InterruptedException e) {
            interrupt();
            return;
        }

        if (message.getType() == MessageType.GAME_CLIENT_UNREGISTER)
            interrupt();

        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
            return;
        }
        try {
            message = receiveMessage();
        } catch (IOException e) {
            interrupt();
            return;
        }

        if (message.getType() == MessageType.GAME_SERVER_STOP)
            interrupt();

        try {
            outputMessageQueue.postMessage(message);
        } catch (InterruptedException ignored) {
            interrupt();
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
}
