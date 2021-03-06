package com.popovych.networking.data.defaults;

import com.popovych.networking.abstracts.Indexer;
import com.popovych.networking.data.ClientData;
import com.popovych.statics.Naming;

import java.net.UnknownHostException;

public class DefaultClientData extends ClientData {

    private static Indexer<Integer> gamerIndexer = null;

    public DefaultClientData(String name, String IP, int port) throws UnknownHostException {
        super(name, IP, port);
    }

    public DefaultClientData() throws UnknownHostException {
        this(String.format(Naming.Templates.gamerName, getIndexer().getIndex()), Naming.Constants.defaultClientIP,
                Naming.Constants.defaultClientPort);
    }

    protected static synchronized Indexer<Integer> getIndexer() {
        if (gamerIndexer == null) {
            return gamerIndexer = new Indexer<>() {

                @Override
                protected void initIndex() {
                    index = 0;
                }

                @Override
                protected void updateIndex() {
                    index++;
                }
            };
        }
        return gamerIndexer;
    }
}
