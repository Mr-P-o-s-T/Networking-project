package com.popovych.game.ui.controller;

import com.popovych.game.interfaces.MainThread;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends DefaultController {
    @FXML
    Label currentPlayerName;

    @FXML
    Button exitButton;

    MainThread clientMainThread = null;

    public ServerCreateController getCreateController() {
        return createController;
    }

    public void setCreateController(ServerCreateController createController) {
        this.createController = createController;
    }

    ServerCreateController createController;

    public MainThread getClientMainThread() {
        return clientMainThread;
    }

    public void setClientMainThread(MainThread clientMainThread) {
        this.clientMainThread = clientMainThread;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void reset() {
        currentPlayerName.setText("");
    }

    @FXML
    void exitAction(ActionEvent event) {
        if (clientMainThread != null) {
            clientMainThread.interrupt();
        }
        if (createController.getServerMainThread() != null) {
            createController.getServerMainThread().interrupt();
        }

        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getStartScreen());
        reset();
    }

    public void triggerExit() {
        exitButton.fire();
    }
}
