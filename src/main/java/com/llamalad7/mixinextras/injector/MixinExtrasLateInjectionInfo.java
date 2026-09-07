package com.llamalad7.mixinextras.injector;

import java.util.List;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/MixinExtrasLateInjectionInfo.class */
public abstract class MixinExtrasLateInjectionInfo extends MixinExtrasInjectionInfo implements LateApplyingInjectorInfo {
    private LateApplyingInjectorInfo injectionInfoToQueue;
    private boolean hasInjectStarted;

    public MixinExtrasLateInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
        this.injectionInfoToQueue = this;
        this.hasInjectStarted = false;
    }

    public MixinExtrasLateInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation, String atKey) {
        super(mixin, method, annotation, atKey);
        this.injectionInfoToQueue = this;
        this.hasInjectStarted = false;
    }

    public void inject() {
        this.hasInjectStarted = true;
        int callbackTotal = 0;
        for (List<InjectionNodes.InjectionNode> nodes : this.targetNodes.values()) {
            callbackTotal += nodes.size();
        }
        for (int i = 0; i < callbackTotal; i++) {
            super.addCallbackInvocation(this.method);
        }
        LateInjectionApplicatorExtension.offerInjection(this.mixin.getTarget(), this.injectionInfoToQueue);
    }

    public void postInject() {
    }

    public void addCallbackInvocation(MethodNode handler) {
        if (!this.hasInjectStarted) {
            super.addCallbackInvocation(handler);
        }
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void lateInject() {
        super.inject();
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void latePostInject() {
        super.postInject();
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void wrap(LateApplyingInjectorInfo outer) {
        this.injectionInfoToQueue = outer;
    }
}
