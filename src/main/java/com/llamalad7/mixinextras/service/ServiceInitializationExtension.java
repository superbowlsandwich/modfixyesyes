package com.llamalad7.mixinextras.service;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/service/ServiceInitializationExtension.class */
class ServiceInitializationExtension implements IExtension {
    private final MixinExtrasService service;
    private boolean initialized;

    public ServiceInitializationExtension(MixinExtrasService service) {
        this.service = service;
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
        if (!this.initialized) {
            this.service.initialize();
            this.initialized = true;
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
