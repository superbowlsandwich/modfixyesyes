package com.llamalad7.mixinextras.wrapper;

import com.llamalad7.mixinextras.sugar.impl.SingleIterationList;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.MixinInternals;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/InjectorWrapperImpl.class */
public abstract class InjectorWrapperImpl {
    private final InjectionInfo wrapperInfo;
    protected final ClassNode classNode;
    private final boolean useGranularInject;

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/InjectorWrapperImpl$Factory.class */
    @FunctionalInterface
    public interface Factory {
        InjectorWrapperImpl create(InjectionInfo injectionInfo, MixinTargetContext mixinTargetContext, MethodNode methodNode, AnnotationNode annotationNode);
    }

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/InjectorWrapperImpl$HandlerCallCallback.class */
    @FunctionalInterface
    public interface HandlerCallCallback {
        void onFound(Target target, InjectionNodes.InjectionNode injectionNode, MethodInsnNode methodInsnNode);
    }

    protected abstract InjectionInfo getDelegate();

    protected abstract MethodNode getHandler();

    protected InjectorWrapperImpl(InjectionInfo wrapper, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation, boolean useGranularInject) {
        this.wrapperInfo = wrapper;
        this.classNode = mixin.getTargetClassNode();
        this.useGranularInject = useGranularInject;
    }

    public boolean usesGranularInject() {
        return this.useGranularInject;
    }

    protected boolean isValid() {
        return getDelegate().isValid();
    }

    public int getOrder() {
        return CompatibilityHelper.getOrder(getDelegate());
    }

    protected void prepare() {
        getDelegate().prepare();
        MethodNode handler = getHandler();
        handler.visibleAnnotations.remove(InjectionInfo.getInjectorAnnotation(CompatibilityHelper.getMixin(this.wrapperInfo).getMixin(), handler));
    }

    protected void preInject() {
        CompatibilityHelper.preInject(getDelegate());
    }

    protected void inject() {
        if (this.useGranularInject) {
            granularInject((target, node, call) -> {
            });
        } else {
            getDelegate().inject();
        }
    }

    protected void granularInject(HandlerCallCallback callback) {
        InjectionInfo delegate = getDelegate();
        if (delegate instanceof WrapperInjectionInfo) {
            WrapperInjectionInfo wrapper = (WrapperInjectionInfo) delegate;
            wrapper.impl.granularInject(callback);
        } else {
            doGranularInject(callback);
        }
    }

    protected void doPostInject(Runnable postInject) {
        postInject.run();
    }

    protected void addCallbackInvocation(MethodNode handler) {
        getDelegate().addCallbackInvocation(handler);
    }

    protected RuntimeException granularInjectNotSupported() {
        return new IllegalStateException(getDelegate().getClass() + " does not support granular injection! Please report to LlamaLad7!");
    }

    private void doGranularInject(HandlerCallCallback callback) {
        InjectionInfo delegate = getDelegate();
        Map<Target, List<InjectionNodes.InjectionNode>> targets = MixinInternals.getTargets(delegate);
        Injector injector = MixinInternals.getInjector(delegate);
        for (Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : targets.entrySet()) {
            Target target = entry.getKey();
            Set<MethodInsnNode> discoveredHandlerCalls = new HashSet<>(findHandlerCalls(target));
            for (InjectionNodes.InjectionNode node : entry.getValue()) {
                inject(injector, target, node);
                for (MethodInsnNode handlerCall : findHandlerCalls(target)) {
                    if (discoveredHandlerCalls.add(handlerCall)) {
                        callback.onFound(target, node, handlerCall);
                    }
                }
            }
            postInject(injector, target, entry.getValue());
        }
        targets.clear();
    }

    private List<MethodInsnNode> findHandlerCalls(Target target) {
        MethodNode handler = getHandler();
        List<MethodInsnNode> result = new ArrayList<>();
        Iterator it = target.iterator();
        while (it.hasNext()) {
            MethodInsnNode methodInsnNode = (AbstractInsnNode) it.next();
            if (methodInsnNode instanceof MethodInsnNode) {
                MethodInsnNode call = methodInsnNode;
                if (call.owner.equals(this.classNode.name) && call.name.equals(handler.name) && call.desc.equals(handler.desc)) {
                    result.add(call);
                }
            }
        }
        return result;
    }

    private static void inject(Injector injector, Target target, InjectionNodes.InjectionNode node) {
        injector.inject(target, new SingleIterationList(Collections.singletonList(node), 0));
    }

    private static void postInject(Injector injector, Target target, List<InjectionNodes.InjectionNode> nodes) {
        injector.inject(target, new SingleIterationList(nodes, 1));
    }
}
