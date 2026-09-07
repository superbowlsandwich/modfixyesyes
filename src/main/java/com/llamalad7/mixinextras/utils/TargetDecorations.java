package com.llamalad7.mixinextras.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.spongepowered.asm.mixin.injection.struct.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/TargetDecorations.class */
public class TargetDecorations {
    private static final Map<Target, Map<String, Object>> IMPL = new WeakHashMap();

    public static boolean has(Target target, String key) {
        return IMPL.containsKey(target) && IMPL.get(target).containsKey(key);
    }

    public static <T> T get(Target target, String str) {
        if (IMPL.containsKey(target)) {
            return (T) IMPL.get(target).get(str);
        }
        return null;
    }

    public static <T> T getOrPut(Target target, String str, Supplier<T> supplier) {
        return (T) IMPL.computeIfAbsent(target, k -> {
            return new HashMap();
        }).computeIfAbsent(str, k2 -> {
            return supplier.get();
        });
    }

    public static void put(Target target, String key, Object value) {
        IMPL.computeIfAbsent(target, k -> {
            return new HashMap();
        }).put(key, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void modify(Target target, String key, UnaryOperator<T> unaryOperator) {
        IMPL.computeIfAbsent(target, k -> {
            return new HashMap();
        }).put(key, unaryOperator.apply(get(target, key)));
    }

    public static void remove(Target target, String key) {
        if (IMPL.containsKey(target)) {
            IMPL.get(target).remove(key);
        }
    }
}
