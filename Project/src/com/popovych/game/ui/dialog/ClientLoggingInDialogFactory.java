package com.popovych.game.ui.dialog;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ClientLoggingInDialogFactory {
    public static Dialog<Pair<String, String>> getDialog() {
        Dialog<Pair<String, String>> loggingInDialog = new Dialog<>();

        loggingInDialog.setTitle("Logging In");
        loggingInDialog.setHeaderText(null);

        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        loggingInDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = loggingInDialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        loggingInDialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        loggingInDialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });


        return loggingInDialog;
    }
}
