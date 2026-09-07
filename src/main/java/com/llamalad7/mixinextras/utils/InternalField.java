package com.llamalad7.mixinextras.utils;

import java.lang.reflect.Field;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/InternalField.class */
interface InternalField<O, T> {
    T get(O o);

    void set(O o, T t);

    static <O, T> InternalField<O, T> of(final Class<?> clazz, final String name) {
        try {
            final Field impl = clazz.getDeclaredField(name);
            impl.setAccessible(true);
            return new InternalField<O, T>() { // from class: com.llamalad7.mixinextras.utils.InternalField.1
                @Override // com.llamalad7.mixinextras.utils.InternalField
                public T get(O o) {
                    try {
                        return (T) impl.get(o);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(String.format("Failed to get %s::%s on %s! Please report to LlamaLad7!", clazz, name, o), e);
                    }
                }

                @Override // com.llamalad7.mixinextras.utils.InternalField
                public void set(O owner, T newValue) {
                    try {
                        impl.set(owner, newValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(String.format("Failed to set %s::%s to %s on %s! Please report to LlamaLad7!", clazz, name, newValue, owner), e);
                    }
                }
            };
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(String.format("Failed to find field %s::%s! Please report to LlamaLad7!", clazz, name), e);
        }
    }

    static <O, T> InternalField<O, T> of(String clazz, String name) {
        try {
            return of(Class.forName(clazz), name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), e);
        }
    }
}
