package com.popovych.networking.server.transmission;

import com.popovych.game.interfaces.ClientDataContainer;
import com.popovych.game.interfaces.Game;
import com.popovych.game.messages.GameClientRegisterMessage;
import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.abstracts.threads.ThreadGroupWorker;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.enumerations.MessageType;
import com.popovych.networking.interfaces.MessageQueueProvider;
import com.popovych.networking.interfaces.args.Arguments;
import com.popovych.networking.interfaces.message.InputMessageQueue;
import com.popovych.networking.interfaces.message.Message;
import com.popovych.networking.interfaces.message.OutputMessageQueue;
import com.popovych.networking.server.message.ServerMessageQueue;
import com.popovych.networking.server.transmission.args.ServerClientTransmitterThreadArguments;
import com.popovych.statics.Naming;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientTransmitterThread extends ThreadGroupWorker {
    protected ServerData sData;
    protected MessageQueueProvider provider;
    protected InputMessageQueue inputMessageQueue = null;
    protected OutputMessageQueue outputMessageQueue = null;
    protected ClientDataContainer container;

    protected Socket socket;
    protected ServerClientTransmitterThread(ThreadGroup group, Indexer<Integer> indexer, Arguments args) {
        super(args, Naming.Templates.serverThread, Naming.Descriptions.clientTransmitterThread, group, indexer,
                false);
    }

    @Override
    protected void processArgs(Arguments arguments) {
        ServerClientTransmitterThreadArguments args = (ServerClientTransmitterThreadArguments) arguments;
        socket = args.getClientHandlerSocket();
        provider = args.getMessageQueueProvider();
        container = args.getClientDataContainer();
    }

    @Override
    protected void prepareTask() {
        inputMessageQueue = provider.getInputMessageQueue();
        outputMessageQueue = provider.getOutputMessageQueue();
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
            message = receiveMessage();
        } catch (EOFException ignored) {
            interrupt();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
            return;
        }

        if (message.getType() == MessageType.GAME_CLIENT_REGISTER) {
            GameClientRegisterMessage registerMessage = (GameClientRegisterMessage) message;
            container.addNewClientData(registerMessage.getClientData());
        } else if (message.getType() == MessageType.GAME_CLIENT_UNREGISTER)
            interrupt();

        try {
            outputMessageQueue.postMessage(message);
        } catch (InterruptedException e) {
            interrupt();
            return;
        }
        try {
            message = inputMessageQueue.pollMessage();
        } catch (InterruptedException e) {
            interrupt();
            return;
        }

        if (message.getType() == MessageType.GAME_SERVER_STOP)
            interrupt();

        try {
            sendMessage(message);
        } catch (IOException e) {
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
