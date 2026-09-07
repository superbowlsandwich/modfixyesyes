package com.llamalad7.mixinextras.versions;

import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/versions/MixinVersionImpl_v0_8_7.class */
public class MixinVersionImpl_v0_8_7 extends MixinVersionImpl_v0_8_4 {
    @Override // com.llamalad7.mixinextras.versions.MixinVersionImpl_v0_8, com.llamalad7.mixinextras.versions.MixinVersion
    public int getOrder(InjectionInfo info) {
        return info.getOrder();
    }
}
