package com.popovych.networking.client.transmission;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.transmission.args.ClientServerTransmitterThreadArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.statics.Naming;

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

    public ClientServerTransmitterThread(ThreadGroup group, ClientServerTransmitterThreadArguments args) {
        super(Naming.Templates.clientThread, Naming.Descriptions.serverTransmitterThread, group);
        cData = args.getCData();
        controller = args.getSearchController();
        sDataProvider = args.getServerDataProvider();

        MessageQueueProvider queueProvider = args.getMessageQueueProvider();
        inputMessageQueue = queueProvider.getInputMessageQueue();
        outputMessageQueue = queueProvider.getOutputMessageQueue();
    }

    @Override
    protected void prepareTask() {
        controller.getDatabaseControllerLocker().lock();
        try {
            if (controller.databaseActionExecutionNow())
                controller.getDatabaseActionCompleteCondition().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            interrupt();
        }
        finally {
            controller.getDatabaseControllerLocker().unlock();
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

    protected void sendMessage(Message message) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }

    protected Message receiveMessage() {
        Message message = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            message = (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            interrupt();
        }
        return message;
    }

    @Override
    protected void runTask() {
        Message message = inputMessageQueue.pollMessage();

        sendMessage(message);
        message = receiveMessage();

        outputMessageQueue.postMessage(message);
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
