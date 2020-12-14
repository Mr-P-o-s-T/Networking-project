package com.popovych.networking.interfaces;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface ServerSearchController {
    boolean searchingNewServerNow();
    void searchNewServer();
    void newServerFound();

    Lock getServerSearchLocker();
    Condition getServerSearchCondition();
    Condition getServerFoundCondition();
}
