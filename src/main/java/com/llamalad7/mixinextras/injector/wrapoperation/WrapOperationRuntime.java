package com.llamalad7.mixinextras.injector.wrapoperation;

import java.util.Arrays;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapoperation/WrapOperationRuntime.class */
public class WrapOperationRuntime {
    public static void checkArgumentCount(Object[] args, int expectedArgumentCount, String expectedTypes) {
        if (args.length != expectedArgumentCount) {
            throw new IncorrectArgumentCountException(String.format("Incorrect number of arguments passed to Operation::call! Expected %s but got %s. Expected types were %s, actual types were %s.", Integer.valueOf(expectedArgumentCount), Integer.valueOf(args.length), expectedTypes, Arrays.stream(args).map(it -> {
                return it.getClass().getName();
            }).collect(Collectors.joining(", ", "[", "]"))));
        }
    }
}
