package com.popovych;

import com.popovych.game.ui.GameScreen;
import com.popovych.game.ui.ServerCreateScreen;
import com.popovych.game.ui.ServerPickScreen;
import com.popovych.game.ui.StartScreen;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.game.ui.enumerations.ScreenType;
import com.popovych.networking.statics.Naming;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    protected DefaultScene loadScene(String sceneFilename) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format(
                Naming.FXMLData.defaultFXMLResourcesPath, sceneFilename)));
        Parent root = loader.load();

        DefaultController.saveScreen(root, sceneFilename);

        return DefaultController.getScreen(ScreenType.getScreenType(sceneFilename));
    }

    @Override
    public void start(Stage stage) {
        try {
            loadScene(Naming.FXMLData.defaultStartScreenFilename);
            loadScene(Naming.FXMLData.defaultServerCreateScreenFilename);
            loadScene(Naming.FXMLData.defaultServerPickScreenFilename);
            loadScene(Naming.FXMLData.defaultGameScreenFilename);
        } catch (IOException e) {
            e.printStackTrace();
            stage.close();
        }

        stage.setScene(DefaultController.getStartScreen());
        stage.setTitle("The Game");
        stage.show();
    }
}