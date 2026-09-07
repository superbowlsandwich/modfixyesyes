package com.llamalad7.mixinextras.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/InternalConstructor.class */
interface InternalConstructor<T> {
    T newInstance(Object... objArr);

    static <T> InternalConstructor<T> of(Class<?> clazz, Class<?>... argTypes) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(argTypes);
            declaredConstructor.setAccessible(true);
            return args -> {
                try {
                    return declaredConstructor.newInstance(args);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Failed to construct %s(%s) with args [%s]! Please report to LlamaLad7!", clazz, Arrays.stream(argTypes).map((v0) -> {
                        return v0.getName();
                    }).collect(Collectors.joining(", ")), Arrays.stream(args).map((v0) -> {
                        return v0.toString();
                    }).collect(Collectors.joining(", "))), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Failed to find constructor %s(%s)! Please report to LlamaLad7!", clazz, Arrays.stream(argTypes).map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(", "))), e);
        }
    }

    static <T> InternalConstructor<T> of(String clazz, Class<?>... argTypes) {
        try {
            return of(Class.forName(clazz), argTypes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), e);
        }
    }
}
