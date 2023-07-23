package com.executor.service.xlsx.framework;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Args {
    private final List<Val> args = new ArrayList<>();
    @Getter
    private final String[] supportedDataTypes;

    public Args(String[] supportedDataTypes) {
        this.supportedDataTypes = supportedDataTypes;
    }

    public Args setNext(ValType valType, String type, Object value) {
        args.add(new Val(supportedDataTypes).of(valType, type, value));
        return this;
    }

    public Val getArg(int position) {
        return args.get(position);
    }
}
