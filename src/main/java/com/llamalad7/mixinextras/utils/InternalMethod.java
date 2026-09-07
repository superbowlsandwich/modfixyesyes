package com.llamalad7.mixinextras.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/InternalMethod.class */
interface InternalMethod<O, R> {
    R call(O o, Object... objArr);

    static <O, R> InternalMethod<O, R> of(Class<?> clazz, String name, Class<?>... argTypes) {
        try {
            Method impl = clazz.getDeclaredMethod(name, argTypes);
            impl.setAccessible(true);
            return (owner, args) -> {
                try {
                    return impl.invoke(owner, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Failed to call %s::%s(%s) with args [%s]! Please report to LlamaLad7!", clazz, name, Arrays.stream(argTypes).map((v0) -> {
                        return v0.getName();
                    }).collect(Collectors.joining(", ")), Arrays.stream(args).map((v0) -> {
                        return v0.toString();
                    }).collect(Collectors.joining(", "))), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Failed to find method %s::%s(%s)! Please report to LlamaLad7!", clazz, name, Arrays.stream(argTypes).map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(", "))), e);
        }
    }

    static <O, R> InternalMethod<O, R> of(String clazz, String name, Class<?>... argTypes) {
        try {
            return of(Class.forName(clazz), name, argTypes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), e);
        }
    }
}
