package com.llamalad7.mixinextras.injector;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/ModifyReturnValueInjectionInfo.class */
@InjectionInfo.AnnotationType(ModifyReturnValue.class)
@InjectionInfo.HandlerPrefix("modifyReturnValue")
public class ModifyReturnValueInjectionInfo extends MixinExtrasInjectionInfo {
    public ModifyReturnValueInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new ModifyReturnValueInjector(this);
    }
}
