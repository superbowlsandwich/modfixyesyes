package com.llamalad7.mixinextras.injector;

import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.Decorations;
import com.llamalad7.mixinextras.utils.InjectorUtils;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import java.util.function.Supplier;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
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
import org.spongepowered.asm.util.Bytecode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/ModifyExpressionValueInjector.class */
public class ModifyExpressionValueInjector extends Injector {
    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("ModifyExpressionValue");

    public ModifyExpressionValueInjector(InjectionInfo info) {
        super(info, "@ModifyExpressionValue");
    }

    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        checkTargetReturnsAValue(target, node);
        checkTargetModifiers(target, false);
        StackExtension stack = new StackExtension(target);
        AbstractInsnNode valueNode = node.getCurrentTarget();
        Type valueType = getReturnType(valueNode);
        boolean shouldPop = false;
        if ((valueNode instanceof TypeInsnNode) && valueNode.getOpcode() == 187) {
            if (!InjectorUtils.isDupedNew(node)) {
                target.insns.insert(valueNode, new InsnNode(89));
                stack.extra(1);
                node.decorate(Decorations.NEW_IS_DUPED, true);
                shouldPop = true;
            }
            valueNode = ASMUtils.findInitNodeFor(target, (TypeInsnNode) valueNode);
        }
        injectValueModifier(target, valueNode, valueType, InjectorUtils.isDupedFactoryRedirect(node), shouldPop, stack);
    }

    private void checkTargetReturnsAValue(Target target, InjectionNodes.InjectionNode node) {
        Type returnType = getReturnType(node.getCurrentTarget());
        if (returnType == Type.VOID_TYPE) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an instruction with a return type of 'void' in %s in %s", this.annotationType, target, this));
        }
        if (returnType == null) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an invalid insn in %s in %s", this.annotationType, target, this));
        }
    }

    private void injectValueModifier(Target target, AbstractInsnNode valueNode, Type valueType, boolean isDupedFactoryRedirect, boolean shouldPop, StackExtension stack) {
        InsnList after = new InsnList();
        invokeHandler(valueType, target, after, stack);
        if (shouldPop) {
            after.add(new InsnNode(87));
        }
        target.insns.insert(getInsertionPoint(valueNode, target, isDupedFactoryRedirect), after);
    }

    private void invokeHandler(Type valueType, Target target, InsnList after, StackExtension stack) {
        Injector.InjectorData handler = new Injector.InjectorData(target, "expression value modifier");
        validateParams(handler, valueType, new Type[]{valueType});
        if (!this.isStatic) {
            after.add(new VarInsnNode(25, 0));
            if (valueType.getSize() == 2) {
                stack.extra(1);
                after.add(new InsnNode(91));
                after.add(new InsnNode(87));
            } else {
                after.add(new InsnNode(95));
            }
        }
        if (handler.captureTargetArgs > 0) {
            pushArgs(target.arguments, after, target.getArgIndices(), 0, handler.captureTargetArgs);
        }
        stack.receiver(this.isStatic);
        stack.capturedArgs(target.arguments, handler.captureTargetArgs);
        invokeHandler(after);
    }

    private AbstractInsnNode getInsertionPoint(AbstractInsnNode valueNode, Target target, boolean isDupedFactoryRedirect) {
        if (!isDupedFactoryRedirect) {
            return valueNode;
        }
        LdcInsnNode ldcInsnNodeFindFactoryRedirectThrowString = InjectorUtils.findFactoryRedirectThrowString(target, valueNode);
        if (ldcInsnNodeFindFactoryRedirectThrowString == null) {
            return valueNode;
        }
        String message = (String) ldcInsnNodeFindFactoryRedirectThrowString.cst;
        Supplier<AbstractInsnNode> failed = () -> {
            LOGGER.warn("Please inform LlamaLad7! Failed to find end of factory redirect throw for '{}'", message);
            return valueNode;
        };
        AbstractInsnNode node = ldcInsnNodeFindFactoryRedirectThrowString.getNext();
        if (node.getOpcode() != 183) {
            return failed.get();
        }
        AbstractInsnNode node2 = node.getNext();
        if (node2.getOpcode() != 191) {
            return failed.get();
        }
        AbstractInsnNode node3 = node2.getNext();
        return !(node3 instanceof LabelNode) ? failed.get() : node3;
    }

    private Type getReturnType(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            return Type.getReturnType(methodInsnNode.desc);
        }
        if (node instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
            if (fieldInsnNode.getOpcode() == 180 || fieldInsnNode.getOpcode() == 178) {
                return Type.getType(fieldInsnNode.desc);
            }
            return Type.VOID_TYPE;
        }
        if (Bytecode.isConstant(node)) {
            return Bytecode.getConstantType(node);
        }
        if ((node instanceof TypeInsnNode) && node.getOpcode() == 187) {
            TypeInsnNode typeInsnNode = (TypeInsnNode) node;
            return Type.getObjectType(typeInsnNode.desc);
        }
        return null;
    }
}
