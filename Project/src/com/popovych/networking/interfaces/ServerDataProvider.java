package com.popovych.networking.interfaces;

import com.popovych.networking.data.ServerData;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface ServerDataProvider {
    boolean isServerChosen();
    void serverChosen();
    void serverReset();

    void setCurrentServerData(ServerData sData);
    ServerData getServerData();

    Lock getChosenServerLocker();
    Condition getChosenServerCondition();
}
