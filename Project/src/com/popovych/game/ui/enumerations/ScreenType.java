package com.popovych.game.ui.enumerations;

import com.popovych.statics.Naming;

public enum ScreenType {
    NONE,
    START,
    SERVER_CREATE,
    SERVER_PICK,
    GAME;

    public static ScreenType getScreenType(String screenFilename) {
        return switch (screenFilename) {
            case Naming.FXMLData.defaultStartScreenFilename -> START;
            case Naming.FXMLData.defaultServerCreateScreenFilename -> SERVER_CREATE;
            case Naming.FXMLData.defaultServerPickScreenFilename -> SERVER_PICK;
            case Naming.FXMLData.defaultGameScreenFilename -> GAME;
            default -> NONE;
        };
    }
}
