package com.llamalad7.mixinextras.injector;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/StackExtension.class */
public class StackExtension {
    private final MethodNode target;

    public StackExtension(Target target) {
        this.target = target.method;
    }

    public void receiver(boolean isStatic) {
        if (!isStatic) {
            this.target.maxStack++;
        }
    }

    public void capturedArgs(Type[] argTypes, int argCount) {
        for (int i = 0; i < argCount; i++) {
            this.target.maxStack += argTypes[i].getSize();
        }
    }

    public void extra(int size) {
        this.target.maxStack += size;
    }

    public void ensureAtLeast(int size) {
        if (this.target.maxStack < size) {
            this.target.maxStack = size;
        }
    }
}
