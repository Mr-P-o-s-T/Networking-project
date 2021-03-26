package com.popovych.statics;

public class Naming {
    public static class Templates {
        public static final String clientThread = "Client thread executor # %d: %s";
        public static final String serverThread = "Server thread executor # %d: %s";
        public static final String databaseThread = "Database thread executor # %d: %s";
        public static final String gamerName = "gamer%d";
        public static final String serverName = "server%d";

    }
    public static class Descriptions {
        public static final String mainThread = "Main thread";

        public static final String serverSearcherThread = "Server searcher thread";
        public static final String serverTransmitterThread = "Server transmitter thread";
        public static final String gameThread = "Game thread";

        public static final String clientHandlerThread = "Client handler thread";
        public static final String serverHandlerThread = "Server handler thread";

        public static final String clientsHandlerThread = "Clients handler thread";
        public static final String serversHandlerThread = "Servers handler thread";

        public static final String clientTransmitterThread = "Client transmitter thread";
        public static final String serverUncoverThread = "Server uncover thread";
        public static final String serverCoverThread = "Server cover thread";
    }
    public static class Groups {
        public static final String client = "Client threads";
        public static final String server = "Server threads";
        public static final String database = "Database threads";
        public static final String clientHandler = "Client handler threads";
        public static final String serverHandler = "Server handler threads";
    }
    public static class Constants {
        public static final String broadcast = "broadcast";
        public static final String localhost = "localhost";

        public static final String defaultClientIP = localhost;
        public static final String defaultServerIP = localhost;
        public static final String defaultDatabaseIP = localhost;
        public static final String defaultBroadcastIP = "255.255.255.255";

        public static final int defaultClientPort = 1488;
        public static final int defaultServerPort = 1489;
        public static final int defaultDatabaseCPort = 1490;
        public static final int getDefaultDatabaseSPort = 1491;

        public static final int minimalFieldSize = 3;
        public static final int maximalFieldSize = 10;

        public static final char XMark = 'X';
        public static final char OMark = 'O';
        public static final char Stalemate = 'S';
    }

    public static class FXMLData {
        public static final String defaultFXMLResourcesPath = "game/ui/fxml/%s";
        public static final String defaultStartScreenFilename = "StartScreen.fxml";
        public static final String defaultServerPickScreenFilename = "ServerPickScreen.fxml";
        public static final String defaultServerCreateScreenFilename = "ServerCreateScreen.fxml";
        public static final String defaultGameScreenFilename = "GameScreen.fxml";
        public static final String waitingStatus = "Waiting other players to connect...";
    }
}
