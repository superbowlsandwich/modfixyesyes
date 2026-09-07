package com.llamalad7.mixinextras.versions;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/versions/MixinVersionImpl_v0_8.class */
public class MixinVersionImpl_v0_8 extends MixinVersion {
    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public RuntimeException makeInvalidInjectionException(InjectionInfo info, String message) {
        return new InvalidInjectionException(info, message);
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public IMixinContext getMixin(InjectionInfo info) {
        return info.getContext();
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public LocalVariableDiscriminator.Context makeLvtContext(InjectionInfo info, Type returnType, boolean argsOnly, Target target, AbstractInsnNode node) {
        return new LocalVariableDiscriminator.Context(returnType, argsOnly, target, node);
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public void preInject(InjectionInfo info) {
        throw new AssertionError("Cannot preInject until 0.8.3");
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public AnnotationNode getAnnotation(InjectionInfo info) {
        return info.getAnnotation();
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersion
    public int getOrder(InjectionInfo info) {
        throw new AssertionError("Cannot getOrder until 0.8.7");
    }
}
