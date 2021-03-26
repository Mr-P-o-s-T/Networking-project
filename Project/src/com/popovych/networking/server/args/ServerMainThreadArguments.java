package com.popovych.networking.server.args;

import com.popovych.game.args.ServerGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseData;
import com.popovych.networking.server.ServerWorkerThreadArguments;
import com.popovych.networking.server.enumerations.ServerWorkerThreadType;

public class ServerMainThreadArguments extends ServerWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        SERVER_DATA(THREAD_TYPE),
        SERVER_DATABASE_DATA(SERVER_DATA),
        SERVER_GAME(SERVER_DATABASE_DATA),
        SERVER_GAME_ARGS(SERVER_GAME);

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

    public ServerMainThreadArguments(ServerData sData, ServerDatabaseData sdbData, Game.ServerGame serverGame,
                                     ServerGameArguments serverArgs) {
        super(ServerWorkerThreadType.MAIN_THREAD);
        args.add(sData);
        args.add(sdbData);
        args.add(serverGame);
        args.add(serverArgs);
    }

    public ServerData getServerData() {
        return (ServerData) args.get(ArgsType.SERVER_DATA.getIndex());
    }

    public ServerDatabaseData getServerDatabaseData() {
        return (ServerDatabaseData) args.get(ArgsType.SERVER_DATABASE_DATA.getIndex());
    }

    public Game.ServerGame getServerGame() {
        return (Game.ServerGame) args.get(ArgsType.SERVER_GAME.getIndex());
    }

    public ServerGameArguments getServerGameArguments() {
        return (ServerGameArguments) args.get(ArgsType.SERVER_GAME_ARGS.getIndex());
    }
}
