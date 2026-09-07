package com.llamalad7.mixinextras.injector.wrapoperation;

import com.llamalad7.mixinextras.injector.StackExtension;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.Decorations;
import com.llamalad7.mixinextras.utils.InjectorUtils;
import com.llamalad7.mixinextras.utils.OperationUtils;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapoperation/WrapOperationInjector.class */
class WrapOperationInjector extends Injector {
    private static final String NPE = Type.getInternalName(NullPointerException.class);
    private final Type operationType;

    public WrapOperationInjector(InjectionInfo info) {
        super(info, "@WrapOperation");
        this.operationType = MixinExtrasService.getInstance().changePackage(Operation.class, Type.getType(CompatibilityHelper.getAnnotation(this.info).desc), WrapOperation.class);
    }

    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        checkTargetModifiers(target, false);
        checkNode(target, node);
        wrapOperation(target, node);
    }

    private void checkNode(Target target, InjectionNodes.InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        MethodInsnNode currentTarget = node.getCurrentTarget();
        if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = currentTarget;
            if (methodInsnNode.name.equals("<init>")) {
                throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is trying to target an <init> call in %s in %s! If this is an instantiation, target the NEW instead.", this.annotationType, target, this));
            }
        } else if (!(currentTarget instanceof FieldInsnNode) && originalTarget.getOpcode() != 193 && originalTarget.getOpcode() != 187) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an invalid insn in %s in %s", this.annotationType, target, this));
        }
    }

    private void wrapOperation(Target target, InjectionNodes.InjectionNode node) {
        StackExtension stack = new StackExtension(target);
        TypeInsnNode currentTarget = node.getCurrentTarget();
        InsnList insns = new InsnList();
        boolean isNew = currentTarget.getOpcode() == 187;
        boolean isDupedNew = InjectorUtils.isDupedNew(node);
        if (isNew) {
            node.decorate(Decorations.WRAPPED, true);
            node = target.addInjectionNode(ASMUtils.findInitNodeFor(target, currentTarget));
        }
        Type[] argTypes = getCurrentArgTypes(node);
        Type returnType = getReturnType(node);
        AbstractInsnNode champion = invokeHandler(target, node, argTypes, returnType, insns, stack);
        if (isDupedNew) {
            target.insns.set(currentTarget, new InsnNode(1));
            stack.extra(1);
            insns.add(new InsnNode(91));
            insns.add(new InsnNode(87));
            insns.add(new InsnNode(87));
            insns.add(new InsnNode(87));
        } else if (isNew) {
            target.insns.set(currentTarget, new InsnNode(0));
            insns.add(new InsnNode(87));
        }
        AbstractInsnNode finalTarget = node.getCurrentTarget();
        target.wrapNode(finalTarget, champion, insns, new InsnList());
        if (isNew) {
            target.getInjectionNode(currentTarget).replace(champion);
        }
        node.decorate(Decorations.WRAPPED, true);
        target.insns.remove(finalTarget);
    }

    private AbstractInsnNode invokeHandler(Target target, InjectionNodes.InjectionNode node, Type[] argTypes, Type returnType, InsnList insns, StackExtension stack) {
        Injector.InjectorData handler = new Injector.InjectorData(target, "operation wrapper");
        boolean hasExtraThis = node.isReplaced() && node.getCurrentTarget().getOpcode() != 184;
        if (hasExtraThis) {
            argTypes = (Type[]) ArrayUtils.remove((Object[]) argTypes, 0);
        }
        Type[] originalArgs = getOriginalArgTypes(node);
        validateParams(handler, returnType, (Type[]) ArrayUtils.add(originalArgs, this.operationType));
        int[] argMap = storeArgs(target, argTypes, insns, 0);
        if (hasExtraThis) {
            insns.add(new InsnNode(87));
        }
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
        }
        pushArgs(this.methodArgs, insns, argMap, 0, originalArgs.length);
        if (hasExtraThis) {
            insns.add(new VarInsnNode(25, 0));
        }
        pushArgs(argTypes, insns, argMap, originalArgs.length, argMap.length);
        makeOperation(target, originalArgs, returnType, node, insns, hasExtraThis, (Type[]) ArrayUtils.subarray(argTypes, originalArgs.length, argTypes.length));
        if (handler.captureTargetArgs > 0) {
            pushArgs(target.arguments, insns, target.getArgIndices(), 0, handler.captureTargetArgs);
        }
        stack.receiver(this.isStatic);
        stack.extra(1);
        stack.capturedArgs(target.arguments, handler.captureTargetArgs);
        AbstractInsnNode champion = super.invokeHandler(insns);
        if (InjectorUtils.isDynamicInstanceofRedirect(node)) {
            insns.add(new InsnNode(95));
            insns.add(new InsnNode(87));
        }
        return champion;
    }

    private void makeOperation(Target target, Type[] argTypes, Type returnType, InjectionNodes.InjectionNode node, InsnList insns, boolean hasExtraThis, Type[] trailingParams) {
        OperationUtils.makeOperation(argTypes, returnType, insns, hasExtraThis, trailingParams, this.classNode, this.operationType, getName(node.getCurrentTarget()), (paramArrayIndex, loadArgs) -> {
            return copyNode(node, paramArrayIndex, target, loadArgs);
        });
    }

    private InsnList copyNode(InjectionNodes.InjectionNode node, int paramArrayIndex, Target target, Consumer<InsnList> loadArgs) {
        AbstractInsnNode ldc;
        MethodInsnNode currentTarget = node.getCurrentTarget();
        InsnList insns = new InsnList();
        if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = currentTarget;
            if (methodInsnNode.name.equals("<init>")) {
                insns.add(new TypeInsnNode(187, methodInsnNode.owner));
                insns.add(new InsnNode(89));
            }
        }
        loadArgs.accept(insns);
        insns.add(currentTarget.clone(Collections.emptyMap()));
        if (InjectorUtils.isDynamicInstanceofRedirect(node)) {
            insns.add(new VarInsnNode(25, paramArrayIndex));
            insns.add(new InsnNode(3));
            insns.add(new InsnNode(50));
            insns.add(new InsnNode(95));
            checkAndMoveNodes(target.insns, insns, currentTarget, it -> {
                return it.getOpcode() == 89;
            }, it2 -> {
                return it2.getOpcode() == 199;
            }, it3 -> {
                return it3.getOpcode() == 187 && ((TypeInsnNode) it3).desc.equals(NPE);
            }, it4 -> {
                return it4.getOpcode() == 89;
            }, it5 -> {
                return (it5 instanceof LdcInsnNode) && (((LdcInsnNode) it5).cst instanceof String);
            }, it6 -> {
                return it6.getOpcode() == 183 && ((MethodInsnNode) it6).owner.equals(NPE);
            }, it7 -> {
                return it7.getOpcode() == 191;
            }, it8 -> {
                return it8 instanceof LabelNode;
            }, it9 -> {
                return it9.getOpcode() == 95;
            }, it10 -> {
                return it10.getOpcode() == 89;
            }, it11 -> {
                return it11.getOpcode() == 198;
            }, it12 -> {
                return it12.getOpcode() == 182 && ((MethodInsnNode) it12).name.equals("getClass");
            }, it13 -> {
                return it13.getOpcode() == 182 && ((MethodInsnNode) it13).name.equals("isAssignableFrom");
            }, it14 -> {
                return it14.getOpcode() == 167;
            }, it15 -> {
                return it15 instanceof LabelNode;
            }, it16 -> {
                return it16.getOpcode() == 87;
            }, it17 -> {
                return it17.getOpcode() == 87;
            }, it18 -> {
                return it18.getOpcode() == 3;
            }, it19 -> {
                return it19 instanceof LabelNode;
            });
        }
        if (InjectorUtils.isDupedFactoryRedirect(node) && (ldc = InjectorUtils.findFactoryRedirectThrowString(target, currentTarget)) != null) {
            checkAndMoveNodes(target.insns, insns, currentTarget, it20 -> {
                return it20.getOpcode() == 89;
            }, it21 -> {
                return it21.getOpcode() == 199;
            }, it22 -> {
                return it22.getOpcode() == 187 && ((TypeInsnNode) it22).desc.equals(NPE);
            }, it23 -> {
                return it23.getOpcode() == 89;
            }, it24 -> {
                return it24 == ldc;
            }, it25 -> {
                return it25.getOpcode() == 183 && ((MethodInsnNode) it25).name.equals("<init>");
            }, it26 -> {
                return it26.getOpcode() == 191;
            }, it27 -> {
                return it27 instanceof LabelNode;
            });
        }
        return insns;
    }

    @SafeVarargs
    private final void checkAndMoveNodes(InsnList from, InsnList to, AbstractInsnNode node, Predicate<AbstractInsnNode>... predicates) {
        AbstractInsnNode current = node.getNext();
        for (Predicate<AbstractInsnNode> predicate : predicates) {
            if (!predicate.test(current)) {
                throw new AssertionError("Failed assertion when wrapping instructions. Please inform LlamaLad7!");
            }
            AbstractInsnNode old = current;
            do {
                current = current.getNext();
            } while (current instanceof FrameNode);
            from.remove(old);
            to.add(old);
        }
    }

    private Type getReturnType(InjectionNodes.InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        MethodInsnNode currentTarget = node.getCurrentTarget();
        if (originalTarget.getOpcode() == 193) {
            return Type.BOOLEAN_TYPE;
        }
        if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = currentTarget;
            if (methodInsnNode.name.equals("<init>")) {
                return Type.getObjectType(methodInsnNode.owner);
            }
            return Type.getReturnType(methodInsnNode.desc);
        }
        if (currentTarget instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) currentTarget;
            if (fieldInsnNode.getOpcode() == 180 || fieldInsnNode.getOpcode() == 178) {
                return Type.getType(fieldInsnNode.desc);
            }
            return Type.VOID_TYPE;
        }
        throw new UnsupportedOperationException();
    }

    private Type[] getOriginalArgTypes(InjectionNodes.InjectionNode node) {
        if (node.hasDecoration(Decorations.NEW_ARG_TYPES)) {
            return (Type[]) node.getDecoration(Decorations.NEW_ARG_TYPES);
        }
        return getEffectiveArgTypes(node.getOriginalTarget());
    }

    private Type[] getCurrentArgTypes(InjectionNodes.InjectionNode node) {
        return getEffectiveArgTypes(node.getCurrentTarget());
    }

    private Type[] getEffectiveArgTypes(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            Type[] args = Type.getArgumentTypes(methodInsnNode.desc);
            if (methodInsnNode.name.equals("<init>")) {
                return args;
            }
            switch (methodInsnNode.getOpcode()) {
                case 183:
                    args = (Type[]) ArrayUtils.add(args, 0, Type.getObjectType(this.classNode.name));
                    break;
                case 184:
                    break;
                default:
                    args = (Type[]) ArrayUtils.add(args, 0, Type.getObjectType(methodInsnNode.owner));
                    break;
            }
            return args;
        }
        if (node instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
            switch (fieldInsnNode.getOpcode()) {
                case 178:
                    return new Type[0];
                case 179:
                    return new Type[]{Type.getType(fieldInsnNode.desc)};
                case 180:
                    return new Type[]{Type.getObjectType(fieldInsnNode.owner)};
                case 181:
                    return new Type[]{Type.getObjectType(fieldInsnNode.owner), Type.getType(fieldInsnNode.desc)};
            }
        }
        if (node.getOpcode() == 193) {
            return new Type[]{Type.getType(Object.class)};
        }
        throw new UnsupportedOperationException();
    }

    private String getName(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            if (methodInsnNode.name.equals("<init>")) {
                String desc = methodInsnNode.owner;
                return "new" + desc.substring(desc.lastIndexOf(47) + 1);
            }
            return ((MethodInsnNode) node).name;
        }
        if (node instanceof FieldInsnNode) {
            return ((FieldInsnNode) node).name;
        }
        if (node.getOpcode() == 193) {
            String desc2 = ((TypeInsnNode) node).desc;
            return "instanceof" + desc2.substring(desc2.lastIndexOf(47) + 1);
        }
        throw new UnsupportedOperationException();
    }
}
