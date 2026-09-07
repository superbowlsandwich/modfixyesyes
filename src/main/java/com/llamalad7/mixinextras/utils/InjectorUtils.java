package com.llamalad7.mixinextras.utils;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.util.Bytecode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/InjectorUtils.class */
public class InjectorUtils {
    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("InjectorUtils");

    public static boolean isVirtualRedirect(InjectionNodes.InjectionNode node) {
        return node.isReplaced() && node.hasDecoration("redirector") && node.getCurrentTarget().getOpcode() != 184;
    }

    public static boolean isDynamicInstanceofRedirect(InjectionNodes.InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        MethodInsnNode currentTarget = node.getCurrentTarget();
        return originalTarget.getOpcode() == 193 && (currentTarget instanceof MethodInsnNode) && Type.getReturnType(currentTarget.desc).equals(Type.getType(Class.class));
    }

    public static void checkForDupedNews(Map<Target, List<InjectionNodes.InjectionNode>> targets) {
        for (Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : targets.entrySet()) {
            for (InjectionNodes.InjectionNode node : entry.getValue()) {
                AbstractInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget.getOpcode() == 187 && currentTarget.getNext().getOpcode() == 89) {
                    node.decorate(Decorations.NEW_IS_DUPED, true);
                }
            }
        }
    }

    public static boolean isDupedNew(InjectionNodes.InjectionNode node) {
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        return currentTarget != null && currentTarget.getOpcode() == 187 && node.hasDecoration(Decorations.NEW_IS_DUPED);
    }

    public static boolean isDupedFactoryRedirect(InjectionNodes.InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        return node.isReplaced() && originalTarget.getOpcode() == 187 && !node.hasDecoration(Decorations.WRAPPED) && node.hasDecoration(Decorations.NEW_IS_DUPED);
    }

    public static AbstractInsnNode findFactoryRedirectThrowString(Target target, AbstractInsnNode start) {
        ListIterator<AbstractInsnNode> it = target.insns.iterator(target.indexOf(start));
        while (it.hasNext()) {
            AbstractInsnNode next = it.next();
            if (next instanceof LdcInsnNode) {
                LdcInsnNode ldc = (LdcInsnNode) next;
                if ((ldc.cst instanceof String) && ((String) ldc.cst).startsWith("@Redirect constructor handler ")) {
                    return ldc;
                }
            }
        }
        LOGGER.warn("Please inform LlamaLad7! Failed to find factory redirect throw string for {}", Bytecode.describeNode(start));
        return null;
    }

    public static void checkForImmediatePops(Map<Target, List<InjectionNodes.InjectionNode>> targets) {
        for (List<InjectionNodes.InjectionNode> nodeList : targets.values()) {
            for (InjectionNodes.InjectionNode node : nodeList) {
                MethodInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget instanceof MethodInsnNode) {
                    Type returnType = Type.getReturnType(currentTarget.desc);
                    if (isTypePoppedByInstruction(returnType, currentTarget.getNext())) {
                        node.decorate(Decorations.POPPED_OPERATION, true);
                    }
                }
            }
        }
    }

    private static boolean isTypePoppedByInstruction(Type type, AbstractInsnNode insn) {
        switch (type.getSize()) {
            case 1:
                return insn.getOpcode() == 87;
            case 2:
                return insn.getOpcode() == 88;
            default:
                return false;
        }
    }
}
