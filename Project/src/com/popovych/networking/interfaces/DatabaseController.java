package com.popovych.networking.interfaces;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface DatabaseController {
    boolean databaseActionExecutionNow();

    Lock getDatabaseControllerLocker();
    Condition getDatabaseActionCompleteCondition();
}
