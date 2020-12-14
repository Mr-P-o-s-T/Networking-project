package com.popovych.networking.client.transmission;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.client.message.ClientMessageQueue;
import com.popovych.networking.client.transmission.args.ClientServerTransmitterArguments;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.interfaces.ServerSearchController;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientServerTransmitter extends ThreadGroupWorker {
    protected ClientData cData;
    protected ServerSearchController controller;
    protected ServerDataProvider provider;
    protected ClientMessageQueue inputMessageQueue;
    protected ClientMessageQueue outputMessageQueue;

    protected Socket socket;

    public ClientServerTransmitter(ThreadGroup group, ClientServerTransmitterArguments args) {
        super(Naming.Templates.clientThread, Naming.Descriptions.serverTransmitterThread, group);
        cData = args.getCData();
        controller = args.getSearchController();
        provider = args.getProvider();
        inputMessageQueue = args.getInputMessageQueue();
    }

    @Override
    protected void prepareTask() {
        controller.getServerSearchLocker().lock();
        try {
            if (controller.searchingNewServerNow())
                controller.getServerFoundCondition().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            interrupt();
        }
        finally {
            controller.getServerSearchLocker().unlock();
        }

        provider.getChosenServerLocker().lock();
        try {
            if (!provider.isServerChosen())
                provider.getChosenServerCondition().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            interrupt();
        } finally {
            provider.getChosenServerLocker().unlock();
        }

        try {
            socket = new Socket(provider.getServerData().getAddress(), provider.getServerData().getPort());
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
        Message message = inputMessageQueue.getMessage();

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
