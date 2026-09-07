package com.llamalad7.mixinextras.sugar.impl;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.llamalad7.mixinextras.sugar.SugarBridge;
import com.llamalad7.mixinextras.sugar.impl.handlers.HandlerInfo;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.GenericParamParser;
import com.llamalad7.mixinextras.utils.MixinInternals;
import com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.asm.MethodNodeEx;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/SugarWrapperImpl.class */
public class SugarWrapperImpl extends InjectorWrapperImpl {
    private final InjectionInfo wrapperInfo;
    private final AnnotationNode originalAnnotation;
    private final List<AnnotationNode> sugarAnnotations;
    private final ArrayList<Type> generics;
    private final MethodNode handler;
    private final InjectionInfo delegate;
    private final SugarInjector sugarInjector;

    protected SugarWrapperImpl(InjectionInfo wrapper, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(wrapper, mixin, method, annotation, true);
        this.wrapperInfo = wrapper;
        method.visibleAnnotations.remove(annotation);
        List list = method.visibleAnnotations;
        AnnotationNode annotationNode = (AnnotationNode) Annotations.getValue(annotation, "original");
        this.originalAnnotation = annotationNode;
        list.add(annotationNode);
        this.sugarAnnotations = (List) Annotations.getValue(annotation, "sugars");
        this.generics = new ArrayList<>(GenericParamParser.getParameterGenerics(method.desc, (String) Annotations.getValue(annotation, "signature")));
        this.handler = prepareHandler(method);
        this.sugarInjector = new SugarInjector(this.wrapperInfo, mixin.getMixin(), this.handler, this.sugarAnnotations, this.generics);
        this.sugarInjector.stripSugar();
        this.delegate = InjectionInfo.parse(mixin, this.handler);
        this.sugarInjector.setTargets(MixinInternals.getTargets(this.delegate));
        if (!isValid()) {
            this.sugarInjector.reSugarHandler();
        }
    }

    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected InjectionInfo getDelegate() {
        return this.delegate;
    }

    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected MethodNode getHandler() {
        return this.handler;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected void prepare() throws MixinException {
        super.prepare();
        this.sugarInjector.prepareSugar();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected void granularInject(InjectorWrapperImpl.HandlerCallCallback callback) throws MixinException {
        Map<Target, List<Pair<InjectionNodes.InjectionNode, MethodInsnNode>>> handlerCallMap = new HashMap<>();
        super.granularInject((target, sourceNode, call) -> {
            callback.onFound(target, sourceNode, call);
            ((List) handlerCallMap.computeIfAbsent(target, k -> {
                return new ArrayList();
            })).add(Pair.of(sourceNode, call));
        });
        this.sugarInjector.reSugarHandler();
        this.sugarInjector.transformHandlerCalls(handlerCallMap);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException */
    @Override // com.llamalad7.mixinextras.wrapper.InjectorWrapperImpl
    protected void doPostInject(Runnable postInject) throws InvalidInjectionException {
        try {
            super.doPostInject(postInject);
        } catch (InvalidInjectionException | InjectionError e) {
            Iterator<SugarApplicationException> it = this.sugarInjector.getExceptions().iterator();
            while (it.hasNext()) {
                e.addSuppressed((SugarApplicationException) it.next());
            }
            throw e;
        }
    }

    private MethodNode prepareHandler(MethodNode original) {
        IMixinInfo mixin = CompatibilityHelper.getMixin(this.wrapperInfo).getMixin();
        HandlerInfo handlerInfo = SugarInjector.getHandlerInfo(mixin, original, this.sugarAnnotations, this.generics);
        if (handlerInfo == null) {
            return original;
        }
        MethodNodeEx newMethod = new MethodNodeEx(original.access, MethodNodeEx.getName(original), original.desc, original.signature, (String[]) original.exceptions.toArray(new String[0]), mixin);
        original.accept(newMethod);
        original.visibleAnnotations.remove(this.originalAnnotation);
        newMethod.name = original.name;
        newMethod.tryCatchBlocks = null;
        newMethod.visitAnnotation(Type.getDescriptor(SugarBridge.class), false);
        handlerInfo.transformHandler(this.classNode, newMethod);
        handlerInfo.transformGenerics(this.generics);
        this.classNode.methods.add(newMethod);
        return newMethod;
    }
}
