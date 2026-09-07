package com.llamalad7.mixinextras.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/transformer/MixinTransformer.class */
public interface MixinTransformer {
    void transform(IMixinInfo iMixinInfo, ClassNode classNode);
}
