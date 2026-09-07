package com.llamalad7.mixinextras.injector.wrapoperation;

import com.llamalad7.mixinextras.injector.MixinExtrasLateInjectionInfo;
import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.Decorations;
import com.llamalad7.mixinextras.utils.InjectorUtils;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapoperation/WrapOperationInjectionInfo.class */
@InjectionInfo.AnnotationType(WrapOperation.class)
@InjectionInfo.HandlerPrefix("wrapOperation")
public class WrapOperationInjectionInfo extends MixinExtrasLateInjectionInfo {
    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("WrapOperation");

    public WrapOperationInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation, determineAtKey(mixin, method, annotation));
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new WrapOperationInjector(this);
    }

    public void prepare() {
        super.prepare();
        InjectorUtils.checkForDupedNews(this.targetNodes);
        for (Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : this.targetNodes.entrySet()) {
            Target target = entry.getKey();
            ListIterator<InjectionNodes.InjectionNode> it = entry.getValue().listIterator();
            while (it.hasNext()) {
                InjectionNodes.InjectionNode node = it.next();
                TypeInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget.getOpcode() == 187) {
                    MethodInsnNode initCall = ASMUtils.findInitNodeFor(target, currentTarget);
                    if (initCall == null) {
                        LOGGER.warn("NEW node {} in {} has no init call?", Bytecode.describeNode(currentTarget), target);
                        it.remove();
                    } else {
                        node.decorate(Decorations.NEW_ARG_TYPES, Type.getArgumentTypes(initCall.desc));
                    }
                }
            }
        }
    }

    protected void parseInjectionPoints(List<AnnotationNode> ats) {
        if (this.atKey.equals("at")) {
            super.parseInjectionPoints(ats);
            return;
        }
        Type returnType = Type.getReturnType(this.method.desc);
        for (AnnotationNode at : ats) {
            this.injectionPoints.add(new BeforeConstant(CompatibilityHelper.getMixin(this), at, returnType.getDescriptor()));
        }
    }

    @Override // com.llamalad7.mixinextras.injector.LateApplyingInjectorInfo
    public String getLateInjectionType() {
        return "WrapOperation";
    }

    private static String determineAtKey(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        boolean at = Annotations.getValue(annotation, "at") != null;
        boolean constant = Annotations.getValue(annotation, "constant") != null;
        if (at != constant) {
            return at ? "at" : "constant";
        }
        Object[] objArr = new Object[3];
        objArr[0] = mixin.getMixin().getClassName();
        objArr[1] = method.name;
        objArr[2] = at ? "both" : "neither";
        throw new IllegalStateException(String.format("@WrapOperation injector %s::%s must specify exactly one of `at` and `constant`, got %s.", objArr));
    }
}
