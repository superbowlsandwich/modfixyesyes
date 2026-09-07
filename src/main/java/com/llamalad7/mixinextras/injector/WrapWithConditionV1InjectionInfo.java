package com.llamalad7.mixinextras.injector;

import com.llamalad7.mixinextras.utils.InjectorUtils;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/WrapWithConditionV1InjectionInfo.class */
@InjectionInfo.AnnotationType(WrapWithCondition.class)
@InjectionInfo.HandlerPrefix("wrapWithCondition")
public class WrapWithConditionV1InjectionInfo extends MixinExtrasInjectionInfo {
    public WrapWithConditionV1InjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new WrapWithConditionInjector(this);
    }

    public void prepare() {
        super.prepare();
        InjectorUtils.checkForImmediatePops(this.targetNodes);
    }
}
