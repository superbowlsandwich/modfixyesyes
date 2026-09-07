package com.llamalad7.mixinextras.injector;

import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/ModifyReturnValueInjector.class */
public class ModifyReturnValueInjector extends Injector {
    public ModifyReturnValueInjector(InjectionInfo info) {
        super(info, "@ModifyReturnValue");
    }

    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        int opcode = node.getCurrentTarget().getOpcode();
        if (opcode < 172 || opcode >= 177) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an invalid insn in %s in %s", this.annotationType, target, this));
        }
        checkTargetModifiers(target, false);
        injectReturnValueModifier(target, node);
    }

    private void injectReturnValueModifier(Target target, InjectionNodes.InjectionNode node) {
        Injector.InjectorData handler = new Injector.InjectorData(target, "return value modifier");
        StackExtension stack = new StackExtension(target);
        InsnList insns = new InsnList();
        validateParams(handler, target.returnType, new Type[]{target.returnType});
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
            if (target.returnType.getSize() == 2) {
                stack.extra(1);
                insns.add(new InsnNode(91));
                insns.add(new InsnNode(87));
            } else {
                insns.add(new InsnNode(95));
            }
        }
        if (handler.captureTargetArgs > 0) {
            pushArgs(target.arguments, insns, target.getArgIndices(), 0, handler.captureTargetArgs);
        }
        stack.receiver(this.isStatic);
        stack.capturedArgs(target.arguments, handler.captureTargetArgs);
        invokeHandler(insns);
        target.insertBefore(node, insns);
    }
}
