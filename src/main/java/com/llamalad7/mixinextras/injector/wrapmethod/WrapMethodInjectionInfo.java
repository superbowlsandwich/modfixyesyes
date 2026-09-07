package com.llamalad7.mixinextras.injector.wrapmethod;

import com.llamalad7.mixinextras.injector.MixinExtrasInjectionInfo;
import java.util.List;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodInjectionInfo.class */
@InjectionInfo.AnnotationType(WrapMethod.class)
@InjectionInfo.HandlerPrefix("wrapMethod")
public class WrapMethodInjectionInfo extends MixinExtrasInjectionInfo {
    public WrapMethodInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new WrapMethodInjector(this);
    }

    protected void parseInjectionPoints(List<AnnotationNode> ats) {
        this.injectionPoints.add(new WrapMethodInjectionPoint());
    }
}
