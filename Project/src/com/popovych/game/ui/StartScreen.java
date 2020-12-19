package com.popovych.game.ui;

import com.popovych.game.ui.abstracts.DefaultScene;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

public class StartScreen extends DefaultScene {

    public StartScreen(Parent parent) {
        super(parent);
    }

    public StartScreen(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    public StartScreen(Parent parent, Paint paint) {
        super(parent, paint);
    }

    public StartScreen(Parent parent, double v, double v1, Paint paint) {
        super(parent, v, v1, paint);
    }

    public StartScreen(Parent parent, double v, double v1, boolean b) {
        super(parent, v, v1, b);
    }

    public StartScreen(Parent parent, double v, double v1, boolean b, SceneAntialiasing sceneAntialiasing) {
        super(parent, v, v1, b, sceneAntialiasing);
    }

}
