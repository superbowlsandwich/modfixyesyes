package com.llamalad7.mixinextras.wrapper.factory;

import com.llamalad7.mixinextras.utils.MixinInternals;
import com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/factory/FactoryRedirectWrapperImpl.class */
public class FactoryRedirectWrapperImpl extends InjectorWrapperImpl {
    private final InjectionInfo delegate;
    private final MethodNode handler;

    protected FactoryRedirectWrapperImpl(InjectionInfo wrapper, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(wrapper, mixin, method, annotation, true);
        method.visibleAnnotations.remove(annotation);
        method.visibleAnnotations.add((AnnotationNode) Annotations.getValue(annotation, "original"));
        this.handler = method;
        this.delegate = InjectionInfo.parse(mixin, method);
    }

    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected InjectionInfo getDelegate() {
        return this.delegate;
    }

    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected MethodNode getHandler() {
        return this.handler;
    }

    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected void granularInject(InjectorWrapperImpl.HandlerCallCallback callback) {
        Map<InjectionNodes.InjectionNode, List<InjectionNodes.InjectionNode>> replacements = new HashMap<>();
        for (Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : MixinInternals.getTargets(this.delegate).entrySet()) {
            for (InjectionNodes.InjectionNode source : entry.getValue()) {
                findReplacedNodes(entry.getKey(), source, it -> {
                    ((List) replacements.computeIfAbsent(source, k -> {
                        return new ArrayList();
                    })).add(it);
                });
            }
        }
        super.granularInject((target, sourceNode, call) -> {
            callback.onFound(target, sourceNode, call);
            List<InjectionNodes.InjectionNode> replacedNodes = (List) replacements.get(sourceNode);
            if (replacedNodes == null) {
                return;
            }
            for (InjectionNodes.InjectionNode replaced : replacedNodes) {
                replaced.replace(call);
            }
        });
    }

    private void findReplacedNodes(Target target, InjectionNodes.InjectionNode source, Consumer<InjectionNodes.InjectionNode> sink) {
        if (source.isRemoved() || source.getCurrentTarget().getOpcode() != 187) {
            return;
        }
        sink.accept(source);
        sink.accept(target.addInjectionNode(target.findInitNodeFor(source.getCurrentTarget())));
    }
}
