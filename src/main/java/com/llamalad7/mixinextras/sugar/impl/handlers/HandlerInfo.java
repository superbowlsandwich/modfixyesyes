package com.llamalad7.mixinextras.sugar.impl.handlers;

import com.llamalad7.mixinextras.sugar.impl.SugarParameter;
import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.UniquenessHelper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/handlers/HandlerInfo.class */
public class HandlerInfo {
    private final Map<Integer, ParameterWrapper> wrappers = new LinkedHashMap();

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/handlers/HandlerInfo$ParameterWrapper.class */
    private static class ParameterWrapper {
        private final Type type;
        private final Type generic;
        private final BiConsumer<InsnList, Runnable> unwrap;

        private ParameterWrapper(Type type, Type generic, BiConsumer<InsnList, Runnable> unwrap) {
            this.type = type;
            this.generic = generic;
            this.unwrap = unwrap;
        }
    }

    public void wrapParameter(SugarParameter param, Type type, Type generic, BiConsumer<InsnList, Runnable> unwrap) {
        this.wrappers.put(Integer.valueOf(param.paramIndex), new ParameterWrapper(type, generic, unwrap));
    }

    public void transformHandler(ClassNode targetClass, MethodNode handler) {
        Type[] paramTypes = Type.getArgumentTypes(handler.desc);
        InsnList insns = new InsnList();
        if (!Bytecode.isStatic(handler)) {
            insns.add(new VarInsnNode(25, 0));
        }
        int index = Bytecode.isStatic(handler) ? 0 : 1;
        for (int i = 0; i < paramTypes.length; i++) {
            VarInsnNode loadInsn = new VarInsnNode(paramTypes[i].getOpcode(21), index);
            ParameterWrapper wrapper = this.wrappers.get(Integer.valueOf(i));
            if (wrapper != null) {
                paramTypes[i] = wrapper.type;
                loadInsn.setOpcode(wrapper.type.getOpcode(21));
                wrapper.unwrap.accept(insns, () -> {
                    insns.add(loadInsn);
                });
            } else {
                insns.add(loadInsn);
            }
            index += paramTypes[i].getSize();
        }
        insns.add(ASMUtils.getInvokeInstruction(targetClass, handler));
        insns.add(new InsnNode(Type.getReturnType(handler.desc).getOpcode(172)));
        handler.instructions = insns;
        handler.localVariables = null;
        handler.name = UniquenessHelper.getUniqueMethodName(targetClass, handler.name + "$mixinextras$bridge");
        handler.desc = Type.getMethodDescriptor(Type.getReturnType(handler.desc), paramTypes);
    }

    public void transformGenerics(ArrayList<Type> generics) {
        for (Map.Entry<Integer, ParameterWrapper> entry : this.wrappers.entrySet()) {
            Type type = entry.getValue().generic;
            generics.set(entry.getKey().intValue(), type);
        }
    }
}
