package com.popovych.networking.client.args;

import com.popovych.game.args.ClientGameArguments;
import com.popovych.game.interfaces.Game;
import com.popovych.networking.client.ClientWorkerThreadArguments;
import com.popovych.networking.client.enumerations.ClientWorkerThreadType;
import com.popovych.networking.data.ClientData;
import com.popovych.networking.data.ServerDatabaseData;

public class ClientMainThreadArguments extends ClientWorkerThreadArguments {
    private enum ArgsType {
        THREAD_TYPE(0),
        CLIENT_DATA(THREAD_TYPE),
        SERVER_DATABASE_DATA(CLIENT_DATA),
        CLIENT_GAME(SERVER_DATABASE_DATA),
        CLIENT_GAME_ARGS(CLIENT_GAME);

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

    public ClientMainThreadArguments(ClientData cData, ServerDatabaseData sdbData, Game.ClientGame clientGame,
                                     ClientGameArguments clientArgs) {
        super(ClientWorkerThreadType.MAIN_THREAD);
        args.add(cData);
        args.add(sdbData);
        args.add(clientGame);
        args.add(clientArgs);
    }

    public ClientData getClientData() {
        return (ClientData) args.get(ArgsType.CLIENT_DATA.getIndex());
    }

    public ServerDatabaseData getServerDatabaseData() {
        return (ServerDatabaseData) args.get(ArgsType.SERVER_DATABASE_DATA.getIndex());
    }

    public Game.ClientGame getClientGame() {
        return (Game.ClientGame) args.get(ArgsType.CLIENT_GAME.getIndex());
    }

    public ClientGameArguments getClientGameArguments() {
        return (ClientGameArguments) args.get(ArgsType.CLIENT_GAME_ARGS.getIndex());
    }
}
