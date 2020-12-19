package com.popovych.networking.server.transmission;

import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.server.message.ServerMessageQueue;
import com.popovych.networking.server.transmission.args.ServerClientTransmitterThreadArguments;
import com.popovych.networking.statics.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientTransmitterThread extends ThreadGroupWorker {
    protected ServerData sData;
    protected ServerMessageQueue inputMessageQueue;
    protected ServerMessageQueue outputMessageQueue;

    protected Socket socket;
    protected ServerClientTransmitterThread(ThreadGroup group, ServerClientTransmitterThreadArguments args) {
        super(Naming.Templates.serverThread, Naming.Descriptions.clientTransmitterThread, group);
        socket = args.getClientHandlerSocket();
        inputMessageQueue = args.getInputMessageQueue();
        outputMessageQueue = args.getOutputMessageQueue();
    }

    @Override
    protected void prepareTask() {

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
        Message message = receiveMessage();

        if (message.getType() == MessageType.GAME_CLIENT_UNREGISTER)
            interrupt();

        outputMessageQueue.postMessage(message);
        message = inputMessageQueue.getMessage();

        if (message.getType() == MessageType.GAME_SERVER_STOP)
            interrupt();

        sendMessage(message);
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
