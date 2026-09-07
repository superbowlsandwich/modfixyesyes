package com.llamalad7.mixinextras.wrapper;

import com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo;
import com.llamalad7.mixinextras.injector.MixinExtrasInjectionInfo;
import com.llamalad7.mixinextras.utils.MixinInternals;
import com.llamalad7.mixinextras.utils.ProxyUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/wrapper/WrapperInjectionInfo.class */
public abstract class WrapperInjectionInfo extends MixinExtrasInjectionInfo implements LateApplyingInjectorInfo {
    final InjectorWrapperImpl impl;
    private final InjectionInfo delegate;
    private final boolean lateApply;

    protected WrapperInjectionInfo(InjectorWrapperImpl.Factory implFactory, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
        this.impl = implFactory.create(this, mixin, method, annotation);
        this.delegate = this.impl.getDelegate();
        boolean lateApply = LateApplyingInjectorInfo.wrap(this.delegate, this);
        if (this.delegate instanceof WrapperInjectionInfo) {
            WrapperInjectionInfo inner = (WrapperInjectionInfo) this.delegate;
            lateApply = inner.lateApply;
        } else if (!lateApply && this.impl.usesGranularInject()) {
            checkDelegate();
        }
        this.lateApply = lateApply;
    }

    protected void readAnnotation() {
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        throw new AssertionError();
    }

    public boolean isValid() {
        return this.impl.isValid();
    }

    public int getOrder() {
        return this.impl.getOrder();
    }

    public void prepare() {
        this.impl.prepare();
    }

    public void preInject() {
        this.impl.preInject();
    }

    public void inject() {
        if (this.lateApply) {
            this.delegate.inject();
        } else {
            this.impl.inject();
        }
    }

    public void postInject() {
        if (!this.lateApply) {
            InjectorWrapperImpl injectorWrapperImpl = this.impl;
            InjectionInfo injectionInfo = this.delegate;
            Objects.requireNonNull(injectionInfo);
            injectorWrapperImpl.doPostInject(injectionInfo::postInject);
        }
    }

    public void addCallbackInvocation(MethodNode handler) {
        this.impl.addCallbackInvocation(handler);
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void lateInject() {
        this.impl.inject();
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void latePostInject() {
        InjectorWrapperImpl injectorWrapperImpl = this.impl;
        LateApplyingInjectorInfo lateApplyingInjectorInfo = (LateApplyingInjectorInfo) ProxyUtils.getProxy(this.delegate, LateApplyingInjectorInfo.class);
        Objects.requireNonNull(lateApplyingInjectorInfo);
        injectorWrapperImpl.doPostInject(lateApplyingInjectorInfo::latePostInject);
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public void wrap(LateApplyingInjectorInfo outer) {
        LateApplyingInjectorInfo.wrap(this.delegate, outer);
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public String getLateInjectionType() {
        if (!this.lateApply) {
            throw new IllegalStateException("Wrapper was asked for its late injection type but does not have one!");
        }
        if (!(this.delegate instanceof LateApplyingInjectorInfo)) {
            return "WrapOperation";
        }
        return this.delegate.getLateInjectionType();
    }

    private void checkDelegate() {
        try {
            if (this.delegate.getClass().getMethod("inject", new Class[0]).getDeclaringClass() != InjectionInfo.class) {
                throw this.impl.granularInjectNotSupported();
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Target, List<InjectionNodes.InjectionNode>> getTargetMap() {
        return MixinInternals.getTargets(this.delegate);
    }
}
