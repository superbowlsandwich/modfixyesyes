package com.llamalad7.mixinextras.injector.wrapmethod;

import java.util.Collection;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodInjectionPoint.class */
class WrapMethodInjectionPoint extends InjectionPoint {
    WrapMethodInjectionPoint() {
    }

    public boolean checkPriority(int targetPriority, int ownerPriority) {
        return true;
    }

    public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
        if (insns.size() == 0) {
            throw new UnsupportedOperationException("Cannot use @WrapMethod on an abstract method!");
        }
        return nodes.add(insns.getFirst());
    }
}
