package com.popovych.game.ui.abstracts;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public abstract class DefaultScene extends Scene {
    public DefaultScene(Parent parent) {
        super(parent);
    }

    public DefaultScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    public DefaultScene(Parent parent, Paint paint) {
        super(parent, paint);
    }

    public DefaultScene(Parent parent, double v, double v1, Paint paint) {
        super(parent, v, v1, paint);
    }

    public DefaultScene(Parent parent, double v, double v1, boolean b) {
        super(parent, v, v1, b);
    }

    public DefaultScene(Parent parent, double v, double v1, boolean b, SceneAntialiasing sceneAntialiasing) {
        super(parent, v, v1, b, sceneAntialiasing);
    }

    public void changeScene(DefaultScene newScene) {
        Stage appStage = (Stage) getWindow();
        appStage.setScene(newScene);
    }

}
