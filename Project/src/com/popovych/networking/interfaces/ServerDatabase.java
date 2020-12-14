package com.popovych.networking.interfaces;

import com.popovych.networking.data.ServerData;
import com.popovych.networking.data.ServerDatabaseResponseData;

public interface ServerDatabase {

    void saveNewAvailableServerData(ServerData sData);
    ServerDatabaseResponseData getSDRData();
}
