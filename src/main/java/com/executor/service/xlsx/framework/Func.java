package com.executor.service.xlsx.framework;

import java.util.function.Function;

public class Func {
    public Val transform(Args args, Function<Args, Val> function) {
        return function.apply(args);
    }
}
