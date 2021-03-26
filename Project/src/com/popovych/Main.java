package com.popovych;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.defaults.DefaultClientGame;
import com.popovych.game.defaults.DefaultGameState;
import com.popovych.game.interfaces.MainThread;
import com.popovych.game.ui.abstracts.DefaultController;
import com.popovych.game.ui.controller.GameController;
import com.popovych.game.ui.controller.ServerCreateController;
import com.popovych.game.ui.controller.ServerPickController;
import com.popovych.game.ui.controller.StartController;
import com.popovych.game.ui.enumerations.ScreenType;
import com.popovych.networking.client.ClientMainThread;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.defaults.DefaultClientData;
import com.popovych.networking.data.defaults.DefaultServerData;
import com.popovych.networking.data.defaults.DefaultServerDatabaseData;
import com.popovych.networking.serverdatabase.DatabaseMainThread;
import com.popovych.networking.serverdatabase.args.DatabaseMainThreadArguments;
import com.popovych.statics.Naming;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main extends Application {

    private static ClientMainThread clientMainThread;

    private static StartController startController;
    private static ServerPickController serverPickController;
    private static ServerCreateController serverCreateController;
    private static GameController gameController;

    private static ClientData clientData;
    private static ServerData serverData;

    static {
        try {
            clientData = new DefaultClientData();
            serverData = new DefaultServerData();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        if (args.length == 0)
            Application.launch(args);
        else {
            if (args[0].equals("--database"))
                new DatabaseMainThread(new DatabaseMainThreadArguments()).join();
        }
    }

    protected DefaultController loadScene(String sceneFilename) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format(
                Naming.FXMLData.defaultFXMLResourcesPath, sceneFilename)));
        Parent root = loader.load();

        DefaultController.saveScreen(root, sceneFilename);

        return loader.getController();
    }

    public static void initThreads() {
        try {
            clientMainThread = new ClientMainThread(clientData, new DefaultServerDatabaseData(),
                    new DefaultClientGame(), new ClientGameArguments(DefaultGameState.class, new
                    GameStateArguments(0, DefaultController.getScreen(ScreenType.GAME))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void updateControllers() {
        serverCreateController.setProvider(clientMainThread.getServerDataProvider());
        serverCreateController.setClientData(clientData);
        serverCreateController.setServerData(serverData);
        serverCreateController.setClientMainThread(clientMainThread);

        serverPickController.setClientData(clientData);
        serverPickController.setServerDataProvider(clientMainThread.getServerDataProvider());
        serverPickController.setClientMainThread(clientMainThread);

        gameController.setClientMainThread(clientMainThread);
        gameController.setCreateController(serverCreateController);
    }

    @Override
    public void start(Stage stage) {
        try {
            startController = (StartController) loadScene(Naming.FXMLData.defaultStartScreenFilename);
            serverCreateController = (ServerCreateController) loadScene(Naming.FXMLData.defaultServerCreateScreenFilename);
            serverPickController = (ServerPickController) loadScene(Naming.FXMLData.defaultServerPickScreenFilename);
            gameController = (GameController) loadScene(Naming.FXMLData.defaultGameScreenFilename);
        } catch (IOException e) {
            e.printStackTrace();
            stage.close();
        }

        initThreads();
        updateControllers();

        stage.setScene(DefaultController.getScreen(ScreenType.START));
        stage.setTitle("The Game");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        clientMainThread.interrupt();
        MainThread serverMainThread;
        if ((serverMainThread = serverCreateController.getServerMainThread()) != null) {
            serverMainThread.interrupt();
        }
    }
}