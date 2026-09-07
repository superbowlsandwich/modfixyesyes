package com.llamalad7.mixinextras.sugar.impl.ref;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/ref/LocalRefRuntime.class */
public class LocalRefRuntime {
    static final byte UNINITIALIZED = 1;
    static final byte DISPOSED = 2;

    public static void checkState(byte state) {
        switch (state) {
            case 0:
                return;
            case UNINITIALIZED /* 1 */:
                throw new IllegalStateException("Use of an uninitialized LocalRef! This should never happen! Please report to LlamaLad7!");
            case DISPOSED /* 2 */:
                throw new IllegalStateException("Use of a disposed LocalRef! You cannot retain these objects past the handler method they were passed to. If you don't think this applies to you then please report your issue to LlamaLad7 as it may be a bug.");
            default:
                throw new IllegalStateException(String.format("Unknown LocalRef state %s?", Byte.valueOf(state)));
        }
    }
}
