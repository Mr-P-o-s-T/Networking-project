package com.popovych.networking.abstracts.args;

import com.popovych.networking.interfaces.args.Arguments;

import java.util.List;

public abstract class DefaultArgumentsImplementation implements Arguments {
    protected List<Object> args = null;

    protected DefaultArgumentsImplementation(List<Object> args) {
        this.args = args;
    }

    @Override
    public List<Object> getArguments() {
        return args;
    }
}
