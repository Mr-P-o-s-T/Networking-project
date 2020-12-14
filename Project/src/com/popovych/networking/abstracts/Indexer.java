package com.popovych.networking.abstracts;

public abstract class Indexer<T> {
    protected T index;

    public T getIndex() {
        T result = index;
        updateIndex();
        return result;
    }

    protected abstract void updateIndex();
}
