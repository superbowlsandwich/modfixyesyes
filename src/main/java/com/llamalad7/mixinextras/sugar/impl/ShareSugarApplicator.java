package com.llamalad7.mixinextras.sugar.impl;

import com.llamalad7.mixinextras.injector.StackExtension;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.throwables.MixinException;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/ShareSugarApplicator.class */
class ShareSugarApplicator extends SugarApplicator {
    ShareSugarApplicator(InjectionInfo info, SugarParameter parameter) {
        super(info, parameter);
    }

    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void validate(Target target, InjectionNodes.InjectionNode node) {
    }

    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void prepare(Target target, InjectionNodes.InjectionNode node) {
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void inject(Target target, InjectionNodes.InjectionNode node, StackExtension stack) throws MixinException {
        ShareInfo info = ShareInfo.getOrCreate(target, this.sugar, this.paramType, this.mixin, stack);
        stack.extra(1);
        target.insns.insertBefore(node.getCurrentTarget(), info.load());
    }
}
