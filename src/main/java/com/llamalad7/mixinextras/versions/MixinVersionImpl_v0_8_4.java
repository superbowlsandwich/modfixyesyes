package com.llamalad7.mixinextras.versions;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/versions/MixinVersionImpl_v0_8_4.class */
public class MixinVersionImpl_v0_8_4 extends MixinVersionImpl_v0_8_3 {
    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public RuntimeException makeInvalidInjectionException(InjectionInfo info, String message) {
        return new InvalidInjectionException(info, message);
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public LocalVariableDiscriminator.Context makeLvtContext(InjectionInfo info, Type returnType, boolean argsOnly, Target target, AbstractInsnNode node) {
        return new LocalVariableDiscriminator.Context(info, returnType, argsOnly, target, node);
    }
}
