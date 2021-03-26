package com.popovych.game.ui.controller;

import com.popovych.game.ui.ServerCreateScreen;
import com.popovych.game.ui.ServerPickScreen;
import com.popovych.game.ui.StartScreen;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.game.ui.GameScreen;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController extends DefaultController {
    @FXML
    public Button newServerBtn;

    @FXML
    public Button connectServerBtn;

    @FXML
    public Button exitBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void newServerAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getServerCreateScreen());
    }

    @FXML
    public void connectServerAction(ActionEvent event) {
        DefaultScene currentScene = (DefaultScene) ((Node)event.getSource()).getScene();
        currentScene.changeScene(getServerPickScreen());
    }

    @FXML
    public void exitAction(ActionEvent event) {
        Stage mainWindowStage = (Stage) exitBtn.getScene().getWindow();
        mainWindowStage.close();
    }

    @Override
    public void reset() {

    }
}
