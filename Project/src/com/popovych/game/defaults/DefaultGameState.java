package com.popovych.game.defaults;

import com.popovych.game.interfaces.Actor;
import com.popovych.game.interfaces.GameState;
import com.popovych.game.args.GameStateArguments;
import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.game.ui.controller.GameController;
import com.popovych.game.ui.dialog.WinStateDialogFactory;
import com.popovych.networking.data.ClientData;
import com.popovych.statics.Naming;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.popovych.statics.Naming.Constants.*;

public class DefaultGameState implements GameState {
    boolean initialize, initializeScene;

    List<Actor> actors = new ArrayList<>();

    int currentActorNum = 0, currentStep = 0;

    public char[][] field;
    int fieldSize, winSequenceLength;

    transient DefaultScene gameScene;
    transient GameController gameController;

    protected class FieldCellActionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Button fieldCell = (Button)event.getSource();
            int rowIndex = GridPane.getRowIndex(fieldCell),
                columnIndex = GridPane.getColumnIndex(fieldCell);

            DefaultActor actor = (DefaultActor) getCurrentActor();
            ReentrantLock actorLock = actor.getActionLock();

            actorLock.lock();
            try {
                field[rowIndex][columnIndex] = ((DefaultActor) getCurrentActor()).getMark();

                fieldCell.setText(Character.toString(field[rowIndex][columnIndex]));

                actor.makeAction();
                actor.getActionLockCondition().signalAll();
            } finally {
                actorLock.unlock();
            }
        }
    }

    protected void initScene() {
        if (gameScene != null) {
            GridPane screenField = (GridPane) ((GridPane)gameScene.getRoot()).getChildren().get(1);
            int i;

            for (i = 0; i < fieldSize; i++) {
                RowConstraints row = new RowConstraints();
                row.setPercentHeight(100.0 / fieldSize);
                screenField.getRowConstraints().add(row);

                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(100.0 / fieldSize);
                screenField.getColumnConstraints().add(column);
            }

            var handler = new FieldCellActionHandler();

            for (i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    Button newBtn = new Button("");
                    newBtn.setMaxWidth(Double.MAX_VALUE);
                    newBtn.setMaxHeight(Double.MAX_VALUE);
                    newBtn.setOnAction(handler);

                    screenField.add(newBtn, i, j);
                }
            }
        }
    }

    void updateScene() {
        if (gameScene != null) {
            GridPane screenField = (GridPane) ((GridPane)gameScene.getRoot()).getChildren().get(1);

            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    Button fieldCell = (Button) screenField.getChildren().get(j * fieldSize + i);
                    fieldCell.setText(Character.toString(field[i][j]));
                    fieldCell.setDisable(field[i][j] != 0);
                }
            }
            Label currentPlayerName = (Label) ((GridPane)gameScene.getRoot()).getChildren().get(0);
            currentPlayerName.setText(String.format("Current player: %s",
                    actors.get(currentActorNum).getRegistered().getName()));
        }
    }

    @Override
    public void resetScene() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j] = 0;
            }
        }

        currentStep = 0;
        currentActorNum = 0;

        if (gameScene != null) {
            Label currentPlayerName = (Label) ((GridPane) gameScene.getRoot()).getChildren().get(0);
            currentPlayerName.setText(Naming.FXMLData.waitingStatus);

            GridPane screenField = (GridPane) ((GridPane)gameScene.getRoot()).getChildren().get(1);

            screenField.getChildren().clear();

            screenField.getRowConstraints().clear();
            screenField.getColumnConstraints().clear();
        }
    }

    public DefaultGameState(GameStateArguments args) {
        this(args, true);
    }

    public DefaultGameState(GameStateArguments args, boolean initialize) {
        this.initialize = initialize;

        fieldSize = args.getFieldSize();
        winSequenceLength = (fieldSize < 2 * 3) ? 3 : fieldSize / 2;
        field = new char[fieldSize][fieldSize];
        gameScene = args.getGameScene();
        initializeScene = args.isInitGameScene();

        if (initializeScene)
            Platform.runLater(this::resetScene);
    }

    @Override
    public void synchronise(GameState newGameState) {
        DefaultGameState newState = (DefaultGameState) newGameState;
        if (initializeScene) {
            Platform.runLater(this::initScene);
            Platform.runLater(this::updateScene);
            initializeScene = false;
        }
        if (initialize) {
            actors.addAll(newState.actors);
            fieldSize = newState.fieldSize;
            field = newState.field;
            currentActorNum = newState.currentActorNum;
            initialize = false;
        }
        else {
            field = newState.field;

            currentActorNum = newState.currentActorNum;

            Platform.runLater(this::updateScene);
        }
    }

    @Override
    public void update() {
        currentActorNum = (currentActorNum + 1) % actors.size();

        currentStep++;
    }

    @Override
    public void saveTo(GameState save) {
        DefaultGameState defaultGameStateSave = (DefaultGameState) save;
        defaultGameStateSave.fieldSize = fieldSize;
        defaultGameStateSave.initialize = initialize;
        defaultGameStateSave.gameScene = gameScene;
        defaultGameStateSave.currentActorNum = currentActorNum;

        defaultGameStateSave.field = field;
    }

    @Override
    public void updateSave(GameState save) {
        DefaultGameState defaultGameStateSave = (DefaultGameState) save;
        defaultGameStateSave.field = field;

        defaultGameStateSave.currentActorNum = currentActorNum;
    }

    @Override
    public void restoreFrom(GameState save) {
        DefaultGameState defaultGameStateSave = (DefaultGameState) save;
        field = defaultGameStateSave.field;

        currentActorNum = defaultGameStateSave.currentActorNum;
    }

    public void activateBoard() {
        if (gameScene != null) {
            GridPane screenField = (GridPane) ((GridPane)gameScene.getRoot()).getChildren().get(1);

            screenField.setDisable(false);
        }
    }

    public void deactivateBoard() {
        if (gameScene != null) {
            GridPane screenField = (GridPane) ((GridPane)gameScene.getRoot()).getChildren().get(1);

            screenField.setDisable(true);
        }
    }

    @Override
    public void addActor(Actor actor) {
        actors.add(actor);
    }

    @Override
    public void addActors(List<Actor> actors) {
        this.actors.addAll(actors);
    }

    @Override
    public Actor getCurrentActor() {
        return actors.get(currentActorNum);
    }

    @Override
    public List<Actor> getActors() {
        return actors;
    }

    protected Actor getNextRegisteringActor() {
        for (Actor actor : actors) {
            if (actor.getRegistered() == null) {
                return actor;
            }
        }
        return null;
    }

    @Override
    public boolean canRegister() {
        return getNextRegisteringActor() != null;
    }

    @Override
    public boolean registerActor(ClientData cData) {
        Actor nextRegistered = getNextRegisteringActor();
        if (nextRegistered != null) {
            return nextRegistered.register(cData);
        }
        return false;
    }

    @Override
    public void unregisterActor(ClientData clientData) {
        for (Actor actor : actors) {
            if (actor.getRegistered() == clientData) {
                actor.unregister();
                break;
            }
        }
    }

    protected boolean checkHorizontalLine(char symbol, int xShift, int yShift) {
        boolean res = false;

        for (int i = 0; i < winSequenceLength; i++) {
            boolean lineRes = true;
            for (int j = 0; j < winSequenceLength; j++) {
                lineRes &= (field[i + yShift][j + xShift] == symbol);
            }
            res |= lineRes;
            if (res)
                break;
        }

        return res;
    }

    protected boolean checkVerticalLine(char symbol, int xShift, int yShift) {
        boolean res = false;

        for (int i = 0; i < winSequenceLength; i++) {
            boolean lineRes = true;
            for (int j = 0; j < winSequenceLength; j++) {
                lineRes &= (field[j + yShift][i + xShift] == symbol);
            }
            res |= lineRes;
            if (res)
                break;
        }

        return res;
    }

    protected boolean checkDiagonal(char symbol, int xShift, int yShift) {
        boolean lRes = true, rRes = true;

        for (int i = 0; i < winSequenceLength; i++) {
            lRes &= (field[i + yShift][i + xShift] == symbol);
            rRes &= (field[winSequenceLength - i + yShift - 1][i + xShift] == symbol);
        }

        return lRes || rRes;
    }

    protected boolean checkField(char symbol) {
        boolean res = false;
        for (int i = 0; i < fieldSize - winSequenceLength + 1; i++) {
            for (int j = 0; j < fieldSize - winSequenceLength + 1; j++) {
                res |= checkHorizontalLine(symbol, i, j) || checkVerticalLine(symbol, i, j) ||
                        checkDiagonal(symbol, i, j);
            }
        }
        return res;
    }

    @Override
    public char checkWinState() {
        if (actors.get(0).getRegistered() != null && actors.get(1).getRegistered() != null) {
            if (checkField(XMark))
                return XMark;
            else if (checkField(OMark))
                return OMark;
            else if (currentStep == fieldSize * fieldSize)
                return Stalemate;
            else
                return 0;
        }
        else if (actors.get(0).getRegistered() == null) {
            return ((DefaultActor)actors.get(1)).getMark();
        }
        else {
            return ((DefaultActor)actors.get(0)).getMark();
        }
    }

    @Override
    public boolean signalWin() {
        Optional<ButtonType> result = WinStateDialogFactory.getWinnerAlert().showAndWait();
        WinStateDialogFactory.Action action = WinStateDialogFactory.getAction(result);

        return action == WinStateDialogFactory.Action.RESTART;
    }

    @Override
    public boolean signalLose() {
        Optional<ButtonType> result = WinStateDialogFactory.getLoserAlert().showAndWait();
        WinStateDialogFactory.Action action = WinStateDialogFactory.getAction(result);

        return action == WinStateDialogFactory.Action.RESTART;
    }

    @Override
    public boolean signalStalemate() {
        Optional<ButtonType> result = WinStateDialogFactory.getStalemateAlert().showAndWait();
        WinStateDialogFactory.Action action = WinStateDialogFactory.getAction(result);

        return action == WinStateDialogFactory.Action.RESTART;
    }
}
