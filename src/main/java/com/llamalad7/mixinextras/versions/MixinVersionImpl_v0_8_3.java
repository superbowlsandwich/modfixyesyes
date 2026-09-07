package com.llamalad7.mixinextras.versions;

import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/versions/MixinVersionImpl_v0_8_3.class */
public class MixinVersionImpl_v0_8_3 extends MixinVersionImpl_v0_8 {
    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public IMixinContext getMixin(InjectionInfo info) {
        return info.getMixin();
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public void preInject(InjectionInfo info) {
        info.preInject();
    }

    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public AnnotationNode getAnnotation(InjectionInfo info) {
        return info.getAnnotationNode();
    }
}
