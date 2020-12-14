package com.popovych.networking.statics;

public class Naming {
    public static class Templates {
        public static final String clientThread = "Client thread executor # %d: %s";
        public static final String serverThread = "Server thread executor # %d: %s";
        public static final String databaseThread = "Server thread executor # %d: %s";
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
    }
    public static class Groups {
        public static final String client = "Client threads";
        public static final String server = "Server threads";
        public static final String database = "Database threads";
        public static final String clientHandler = "Client handler threads";
        public static final String serverHandler = "Server handler threads";
    }
    public static class Constants {
        public static final int bufferSize = 1500;
    }
}
