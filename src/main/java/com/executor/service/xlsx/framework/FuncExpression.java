package com.executor.service.xlsx.framework;

import lombok.Builder;

import java.util.function.Function;

@Builder
public class FuncExpression {
    private Args args;
    private Function<Args, Val> function;
    @Builder.Default
    private Func func = new Func();

    public FuncExpression setArgs(Args args) {
        this.args = args;
        return this;
    }

    public Val transform() {
        return func.transform(args, function);
    }
}
