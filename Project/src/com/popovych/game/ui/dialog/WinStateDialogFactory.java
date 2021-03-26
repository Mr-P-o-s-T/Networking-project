package com.popovych.game.ui.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WinStateDialogFactory {
    public enum Action {
        NONE,
        RESTART
    }

    public static ButtonType buttonTypeRestart = new ButtonType("Restart");
    //public static ButtonType buttonTypeDisconnect = new ButtonType("Disconnect");

    public static Alert getWinnerAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Congratulations!");
        alert.setContentText("You win!");
        alert.getButtonTypes().setAll(buttonTypeRestart);
        return alert;
    }

    public static Alert getLoserAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("...");
        alert.setContentText("You lose!");
        alert.getButtonTypes().setAll(buttonTypeRestart);
        return alert;
    }

    public static Alert getStalemateAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("...");
        alert.setContentText("Stalemate!");
        alert.getButtonTypes().setAll(buttonTypeRestart);
        return alert;
    }

    public static Action getAction(Optional<ButtonType> optionalResult) {
        AtomicBoolean isPresent = new AtomicBoolean(true);
        AtomicReference<Action> resultAction = new AtomicReference<>();
        optionalResult.ifPresentOrElse(buttonType -> {
            if (buttonType == buttonTypeRestart){
                resultAction.set(Action.RESTART);
            }
        }, () -> {
            isPresent.set(false);
        });

        if (isPresent.get()) {
            return resultAction.get();
        }
        return Action.NONE;
    }
}
