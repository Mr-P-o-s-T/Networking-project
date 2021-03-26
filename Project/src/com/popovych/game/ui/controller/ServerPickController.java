package com.popovych.game.ui.controller;

import com.popovych.game.interfaces.MainThread;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.game.ui.dialog.ClientLoggingInDialogFactory;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseResponseData;
import com.popovych.networking.interfaces.DatabaseController;
import com.popovych.networking.interfaces.ServerDataProvider;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerPickController extends DefaultController {

    static class ServerListCell extends ListCell<ServerData> {
        @Override
        protected void updateItem(ServerData node, boolean b) {
            super.updateItem(node, b);
            if (node != null) {
                Label serverName = new Label(node.getName());
                Label serverIP = new Label(node.getAddress().toString());
                HBox serverInfo = new HBox(15, serverName, serverIP);

                setGraphic(serverInfo);
            }
            else
                setGraphic(null);
        }
    }

    @FXML
    ListView<ServerData> serverList;

    @FXML
    Button searchButton;

    @FXML
    Button connectButton;

    @FXML
    Button backButton;

    protected ClientData clientData = null;

    protected ServerDataProvider provider = null;

    protected MainThread clientMainThread = null;

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public ServerDataProvider getProvider() {
        return provider;
    }

    public void setServerDataProvider(ServerDataProvider provider) {
        this.provider = provider;
    }

    public MainThread getClientMainThread() {
        return clientMainThread;
    }

    public void setClientMainThread(MainThread clientMainThread) {
        this.clientMainThread = clientMainThread;
    }

    ObservableList<ServerData> sData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverList.setItems(sData);
        serverList.setCellFactory(serverDataListView -> new ServerListCell());
        serverList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ServerData>() {
            @Override
            public void changed(ObservableValue<? extends ServerData> observableValue, ServerData serverData,
                                ServerData t1) {
                connectButton.setDisable(t1 == null);
            }
        });
    }

    @Override
    public void reset() {
        serverList.getItems().clear();
    }

    public void searchAction(ActionEvent event) {
        sData.clear();
        clientMainThread.SpawnDatabaseThread();
        DatabaseController controller = clientMainThread.getDatabaseController();
        controller.getDatabaseControllerLocker().lock();
        try {
            if (controller.databaseActionExecutionNow()) {
                controller.getDatabaseActionCompleteCondition().await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            controller.getDatabaseControllerLocker().unlock();
        }

        ServerDatabaseResponseData responseData = controller.getServerDatabaseResponseData();
        sData.addAll(responseData.getAvailableServersData());
    }

    public void connectAction(ActionEvent event) {
        ServerData sData = serverList.getSelectionModel().getSelectedItem();
        if (provider != null) {
            provider.getChosenServerLocker().lock();
            try {
                provider.setCurrentServerData(sData);
                provider.serverChosen();
                provider.getChosenServerCondition().signalAll();
            } finally {
                provider.getChosenServerLocker().unlock();
            }
        }

        Optional<Pair<String, String>> credentials = ClientLoggingInDialogFactory.getDialog().showAndWait();

        AtomicBoolean isPresent = new AtomicBoolean(true);

        credentials.ifPresentOrElse(usernamePassword -> {
            clientData.setName(usernamePassword.getKey());
            clientData.setLoggingPassword(usernamePassword.getValue());
        }, () -> {
            isPresent.set(false);
        });

        if (isPresent.get()) {

            clientMainThread.SpawnGameThread();
            clientMainThread.SpawnTransmitterThread();

            DefaultScene currentScene = (DefaultScene) ((Node) event.getSource()).getScene();
            currentScene.changeScene(getGameScreen());
            reset();
        }
    }

    public void backAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getStartScreen());
        reset();
    }
}
