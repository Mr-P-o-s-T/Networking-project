package com.popovych.game.ui.abstracts;

import com.popovych.game.ui.ServerCreateScreen;
import com.popovych.game.ui.ServerPickScreen;
import com.popovych.game.ui.StartScreen;
import com.popovych.game.ui.GameScreen;
import com.popovych.game.ui.enumerations.ScreenType;
import com.popovych.statics.Naming;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class DefaultController implements Initializable {
    protected static DefaultScene[] scenes = new DefaultScene[ScreenType.values().length];

    public static StartScreen getStartScreen() {
        return (StartScreen) scenes[ScreenType.START.ordinal()];
    }

    public static void setStartScreen(DefaultScene startScreen) {
        scenes[ScreenType.START.ordinal()] = startScreen;
    }

    public static ServerPickScreen getServerPickScreen() {
        return (ServerPickScreen) scenes[ScreenType.SERVER_PICK.ordinal()];
    }

    public static void setServerPickScreen(DefaultScene serverPickScreen) {
        scenes[ScreenType.SERVER_PICK.ordinal()] = serverPickScreen;
    }

    public static ServerCreateScreen getServerCreateScreen() {
        return (ServerCreateScreen) scenes[ScreenType.SERVER_CREATE.ordinal()];
    }

    public static void setServerCreateScreen(DefaultScene serverCreateScreen) {
        scenes[ScreenType.SERVER_CREATE.ordinal()] = serverCreateScreen;
    }

    public static GameScreen getGameScreen() {
        return (GameScreen) scenes[ScreenType.GAME.ordinal()];
    }

    public static void setGameScreen(DefaultScene gameScreen) {
        scenes[ScreenType.GAME.ordinal()] = gameScreen;
    }

    public static void saveScreen(Parent root, String screenFilename) {
        switch (screenFilename) {
            case Naming.FXMLData.defaultStartScreenFilename -> setStartScreen(new StartScreen(root));
            case Naming.FXMLData.defaultServerCreateScreenFilename -> setServerCreateScreen(new
                    ServerCreateScreen(root));
            case Naming.FXMLData.defaultServerPickScreenFilename -> setServerPickScreen(new
                    ServerPickScreen(root));
            case Naming.FXMLData.defaultGameScreenFilename -> setGameScreen(new GameScreen(root));
        }
    }

    public static DefaultScene getScreen(ScreenType screenType) {
        return switch (screenType) {
            case NONE -> null;
            case START -> getStartScreen();
            case SERVER_CREATE -> getServerCreateScreen();
            case SERVER_PICK -> getServerPickScreen();
            case GAME -> getGameScreen();
        };
    }

    public abstract void reset();
}
