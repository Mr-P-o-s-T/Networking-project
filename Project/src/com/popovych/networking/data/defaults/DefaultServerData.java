package com.popovych.networking.data.defaults;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.data.ServerData;
import com.popovych.networking.statics.Naming;

import java.net.UnknownHostException;

public class DefaultServerData extends ServerData {

    private static Indexer<Integer> gamerIndexer = null;

    public DefaultServerData() throws UnknownHostException {
        this(String.format(Naming.Templates.serverName, getIndexer().getIndex()), "", "localhost", 1489);
    }

    public DefaultServerData(String name, String password, String IP, int port) throws UnknownHostException {
        super(name, password, IP, port);
    }

    protected static synchronized Indexer<Integer> getIndexer() {
        if (gamerIndexer == null) {
            return gamerIndexer = new Indexer<>() {
                @Override
                protected void updateIndex() {
                    index++;
                }
            };
        }
        return gamerIndexer;
    }
}
