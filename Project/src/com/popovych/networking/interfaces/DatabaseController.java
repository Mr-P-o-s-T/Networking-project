package com.popovych.networking.interfaces;

import com.popovych.networking.data.ServerDatabaseResponseData;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface DatabaseController {
    boolean databaseActionExecutionNow();

    Lock getDatabaseControllerLocker();
    Condition getDatabaseActionCompleteCondition();

    ServerDatabaseResponseData getServerDatabaseResponseData();
}
