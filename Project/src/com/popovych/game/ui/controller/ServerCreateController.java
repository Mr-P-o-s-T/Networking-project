package com.popovych.game.ui.controller;

import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.networking.statics.Naming;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import javax.naming.Name;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerCreateController extends DefaultController {
    @FXML
    TextField serverName;

    @FXML
    PasswordField serverPassword;

    @FXML
    Spinner<Integer> fieldSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverName.setText(Naming.Constants.defaultServerIP);
    }

    @FXML
    public void startAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getGameScreen());
    }

    @FXML
    public void backAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getStartScreen());
    }
}
