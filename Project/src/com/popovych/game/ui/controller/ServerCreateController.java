package com.popovych.game.ui.controller;

import com.popovych.game.args.GameModeArguments;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.defaults.DefaultActorSpawner;
import com.popovych.game.defaults.DefaultGameMode;
import com.popovych.game.defaults.DefaultGameState;
import com.popovych.game.defaults.DefaultServerGame;
import com.popovych.game.interfaces.MainThread;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.game.ui.dialog.ClientLoggingInDialogFactory;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.interfaces.ServerDataProvider;
import com.popovych.networking.server.ServerMainThread;
import com.popovych.statics.Naming;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerCreateController extends DefaultController {
    @FXML
    TextField serverName;

    @FXML
    PasswordField serverPassword;

    @FXML
    Spinner<Integer> fieldSize;

    protected ServerDataProvider provider = null;

    ClientData clientData = null;
    protected ServerData serverData = null;
    protected MainThread clientMainThread = null, serverMainThread = null;

    public ServerDataProvider getProvider() {
        return provider;
    }

    public void setProvider(ServerDataProvider provider) {
        this.provider = provider;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void setServerData(ServerData serverData) {
        this.serverData = serverData;
        serverName.setText(serverData.getName());
    }

    public MainThread getClientMainThread() {
        return clientMainThread;
    }

    public void setClientMainThread(MainThread clientMainThread) {
        this.clientMainThread = clientMainThread;
    }

    public MainThread getServerMainThread() {
        return serverMainThread;
    }

    public void setServerMainThread(MainThread serverMainThread) {
        this.serverMainThread = serverMainThread;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void startAction(ActionEvent event) {
        serverData.setName(serverName.getText());
        serverData.setPassword(serverPassword.getText());

        Optional<Pair<String, String>> credentials = ClientLoggingInDialogFactory.getDialog().showAndWait();

        AtomicBoolean isPresent = new AtomicBoolean(true);

        credentials.ifPresentOrElse(usernamePassword -> {
            clientData.setName(usernamePassword.getKey());
            clientData.setLoggingPassword(usernamePassword.getValue());
        }, () -> {
            isPresent.set(false);
        });

        if (isPresent.get()) {
            try {
                setServerMainThread(new ServerMainThread(serverData, new DefaultServerDatabaseData(),
                        new DefaultServerGame(), new ServerGameArguments(DefaultGameMode.class,
                        new GameModeArguments(DefaultGameState.class, new GameStateArguments(fieldSize.getValue(), null),
                                new DefaultActorSpawner()), serverData)));

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            if (provider != null) {
                provider.getChosenServerLocker().lock();
                try {
                    provider.serverChosen();
                    provider.setCurrentServerData(serverData);
                    provider.getChosenServerCondition().signalAll();
                } finally {
                    provider.getChosenServerLocker().unlock();
                }
            }

            clientMainThread.SpawnGameThread();

            serverMainThread.SpawnGameThread();
            serverMainThread.SpawnTransmitterThread();

            clientMainThread.SpawnTransmitterThread();

            DefaultScene currentScene = (DefaultScene) ((Node) event.getSource()).getScene();
            currentScene.changeScene(getGameScreen());
        }
    }

    @FXML
    public void backAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getStartScreen());
        reset();
    }

    @Override
    public void reset() {
        serverName.clear();
        serverPassword.clear();
        fieldSize.getValueFactory().setValue(Naming.Constants.minimalFieldSize);
    }
}
