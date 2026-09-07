package com.llamalad7.mixinextras.injector.wrapmethod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.sugar.impl.ShareInfo;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.util.Annotations;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodInjector.class */
public class WrapMethodInjector extends Injector {
    private final Type operationType;
    private final List<ShareInfo> shares;

    public WrapMethodInjector(InjectionInfo info) {
        super(info, "@WrapMethod");
        this.operationType = MixinExtrasService.getInstance().changePackage(Operation.class, Type.getType(CompatibilityHelper.getAnnotation(this.info).desc), WrapMethod.class);
        this.shares = new ArrayList();
    }

    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        checkTargetModifiers(target, true);
        checkSignature(target);
        this.info.addCallbackInvocation(this.methodNode);
        WrapMethodApplicatorExtension.offerWrapper(target, this.methodNode, this.operationType, this.shares);
    }

    private void checkSignature(Target target) {
        ShareInfo share;
        Injector.InjectorData handler = new Injector.InjectorData(target, "method wrapper");
        String description = String.format("%s %s %s from %s", this.annotationType, handler, this, CompatibilityHelper.getMixin(this.info));
        if (!this.returnType.equals(target.returnType)) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s has an incorrect return type Expected %s but got %s!", description, target, target.returnType, this.returnType));
        }
        int argIndex = 0;
        while (argIndex < target.arguments.length) {
            Type theirType = target.arguments[argIndex];
            if (argIndex >= this.methodArgs.length) {
                throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s doesn't have enough parameters!", description, target));
            }
            Type ourType = this.methodArgs[argIndex];
            if (ourType.equals(theirType)) {
                argIndex++;
            } else {
                throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s has a mismatching param at index %s! Expected %s but got %s", description, target, Integer.valueOf(argIndex), theirType, ourType));
            }
        }
        if (argIndex < this.methodArgs.length) {
            int i = argIndex;
            int argIndex2 = argIndex + 1;
            if (this.methodArgs[i].equals(this.operationType)) {
                List<AnnotationNode> sugars = (List) Annotations.getValue(CompatibilityHelper.getAnnotation(this.info), "sugars");
                if (sugars != null) {
                    for (int i2 = 0; i2 < argIndex2; i2++) {
                        AnnotationNode sugar = sugars.get(i2);
                        if (MixinExtrasService.getInstance().isClassOwned(Type.getType(sugar.desc).getClassName())) {
                            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s has sugar on a non-trailing param which is not allowed!", description, target));
                        }
                    }
                }
                while (argIndex2 < this.methodArgs.length) {
                    if (sugars == null || (share = ShareInfo.getOrCreate(target, sugars.get(argIndex2), this.methodArgs[argIndex2], CompatibilityHelper.getMixin(this.info).getMixin(), null)) == null) {
                        throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s has an excess parameter at index %s!", description, target, Integer.valueOf(argIndex2)));
                    }
                    this.shares.add(share);
                    argIndex2++;
                }
                return;
            }
        }
        throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s targeting %s is missing Operation parameter!", description, target));
    }
}
