package com.llamalad7.mixinextras.wrapper.factory;

import com.llamalad7.mixinextras.transformer.MixinTransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.util.Annotations;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/factory/FactoryRedirectWrapperMixinTransformer.class */
public class FactoryRedirectWrapperMixinTransformer implements MixinTransformer {
    @Override // com.llamalad7.mixinextras.transformer.MixinTransformer
    public void transform(IMixinInfo mixinInfo, ClassNode mixinNode) {
        AnnotationNode at;
        for (MethodNode method : mixinNode.methods) {
            AnnotationNode redirect = Annotations.getVisible(method, Redirect.class);
            if (redirect != null && (at = (AnnotationNode) Annotations.getValue(redirect, "at")) != null) {
                String value = (String) Annotations.getValue(at);
                if ("NEW".equals(value)) {
                    wrapInjectorAnnotation(method, redirect);
                }
            }
        }
    }

    private void wrapInjectorAnnotation(MethodNode method, AnnotationNode redirect) {
        AnnotationNode wrapped = new AnnotationNode(Type.getDescriptor(FactoryRedirectWrapper.class));
        wrapped.visit("original", redirect);
        method.visibleAnnotations.remove(redirect);
        method.visibleAnnotations.add(wrapped);
    }
}
