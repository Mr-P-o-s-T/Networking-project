package com.popovych.networking.abstracts;

public abstract class Indexer<T> {
    protected T index;

    public Indexer() {
        initIndex();
    }

    public T getIndex() {
        T result = index;
        updateIndex();
        return result;
    }

    protected abstract void initIndex();
    protected abstract void updateIndex();
}
