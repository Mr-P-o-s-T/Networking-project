package com.popovych.game.args;

import com.popovych.game.ui.abstracts.DefaultScene;
import com.popovych.networking.abstracts.args.DefaultArgumentsImplementation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

public class GameStateArguments extends DefaultArgumentsImplementation {
    private enum ArgsType {
        FIELD_SIZE(0),
        GAME_SCENE(FIELD_SIZE),
        INIT_GAME_SCENE(GAME_SCENE);

        private final int index;
        private final int autoinc;

        ArgsType(ArgsType prevType) {
            this(prevType.index + prevType.autoinc, prevType.autoinc);
        }

        ArgsType(int i) {
            this(i, 1);
        }

        ArgsType(int i, int autoinc) {
            index = i;
            this.autoinc = autoinc;
        }

        public int getIndex() {
            return index;
        }
    }

    public GameStateArguments(GameStateArguments gsArgs, boolean initGameScene) {
        super(new ArrayList<>());
        args.add(gsArgs.getFieldSize());
        args.add(gsArgs.getGameScene());
        args.add(initGameScene);
    }

    public GameStateArguments(int fieldSize, DefaultScene gameScene) {
        super(new ArrayList<>());
        args.add(fieldSize);
        args.add(gameScene);
        args.add(false);
    }

    public int getFieldSize() {
        return (int) args.get(ArgsType.FIELD_SIZE.getIndex());
    }

    public DefaultScene getGameScene() {
        return (DefaultScene) args.get(ArgsType.GAME_SCENE.getIndex());
    }

    public boolean isInitGameScene() {
        return (boolean) args.get(ArgsType.INIT_GAME_SCENE.getIndex());
    }
}
