package com.llamalad7.mixinextras.utils;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperationRuntime;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.asm.ASM;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/OperationUtils.class */
public class OperationUtils {
    private static final Handle LMF_HANDLE = new Handle(6, "java/lang/invoke/LambdaMetafactory", "metafactory", Bytecode.generateDescriptor(CallSite.class, new Object[]{MethodHandles.Lookup.class, String.class, MethodType.class, MethodType.class, MethodHandle.class, MethodType.class}), false);

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/OperationUtils$OperationContents.class */
    @FunctionalInterface
    public interface OperationContents {
        InsnList generate(int i, Consumer<InsnList> consumer);
    }

    public static void makeOperation(Type[] argTypes, Type returnType, InsnList insns, boolean virtual, Type[] trailingParams, ClassNode classNode, Type operationType, String name, OperationContents contents) {
        Type objectType;
        Type[] descriptorArgs = trailingParams;
        if (virtual) {
            descriptorArgs = (Type[]) ArrayUtils.add(descriptorArgs, 0, Type.getObjectType(classNode.name));
        }
        String methodDescriptor = Type.getMethodDescriptor(operationType, descriptorArgs);
        Handle handle = LMF_HANDLE;
        Object[] objArr = new Object[3];
        objArr[0] = Type.getMethodType(Type.getType(Object.class), new Type[]{Type.getType(Object[].class)});
        objArr[1] = generateSyntheticBridge(argTypes, returnType, virtual, trailingParams, name, classNode, contents);
        if (ASMUtils.isPrimitive(returnType)) {
            objectType = Type.getObjectType(returnType == Type.VOID_TYPE ? "java/lang/Void" : Bytecode.getBoxingType(returnType));
        } else {
            objectType = returnType;
        }
        objArr[2] = Type.getMethodType(objectType, new Type[]{Type.getType(Object[].class)});
        insns.add(new InvokeDynamicInsnNode("call", methodDescriptor, handle, objArr));
    }

    private static Handle generateSyntheticBridge(final Type[] argTypes, final Type returnType, final boolean virtual, final Type[] boundParams, String name, ClassNode classNode, final OperationContents contents) {
        Type objectType;
        int i = ASM.API_VERSION;
        int i2 = 4098 | (virtual ? 0 : 8);
        String uniqueMethodName = UniquenessHelper.getUniqueMethodName(classNode, "mixinextras$bridge$" + name);
        if (ASMUtils.isPrimitive(returnType)) {
            objectType = Type.getObjectType(returnType == Type.VOID_TYPE ? "java/lang/Void" : Bytecode.getBoxingType(returnType));
        } else {
            objectType = returnType;
        }
        MethodNode method = new MethodNode(i, i2, uniqueMethodName, Bytecode.generateDescriptor(objectType, (Type[]) ArrayUtils.add(boundParams, Type.getType(Object[].class))), (String) null, (String[]) null);
        method.instructions = new InsnList() { // from class: com.llamalad7.mixinextras.utils.OperationUtils.1
            {
                int paramArrayIndex = Arrays.stream(boundParams).mapToInt((v0) -> {
                    return v0.getSize();
                }).sum() + (virtual ? 1 : 0);
                add(new VarInsnNode(25, paramArrayIndex));
                add(new IntInsnNode(16, argTypes.length));
                add(new LdcInsnNode(Arrays.stream(argTypes).map((v0) -> {
                    return v0.getClassName();
                }).collect(Collectors.joining(", ", "[", "]"))));
                add(new MethodInsnNode(184, Type.getInternalName(WrapOperationRuntime.class), "checkArgumentCount", Bytecode.generateDescriptor(Void.TYPE, new Object[]{Object[].class, Integer.TYPE, String.class}), false));
                if (virtual) {
                    add(new VarInsnNode(25, 0));
                }
                Type[] typeArr = argTypes;
                boolean z = virtual;
                Type[] typeArr2 = boundParams;
                Consumer<InsnList> loadArgs = insns -> {
                    insns.add(new VarInsnNode(25, paramArrayIndex));
                    for (int i3 = 0; i3 < typeArr.length; i3++) {
                        Type argType = typeArr[i3];
                        insns.add(new InsnNode(89));
                        insns.add(new IntInsnNode(16, i3));
                        insns.add(new InsnNode(50));
                        if (ASMUtils.isPrimitive(argType)) {
                            insns.add(new TypeInsnNode(192, Bytecode.getBoxingType(argType)));
                            insns.add(new MethodInsnNode(182, Bytecode.getBoxingType(argType), Bytecode.getUnboxingMethod(argType), Type.getMethodDescriptor(argType, new Type[0]), false));
                        } else {
                            insns.add(new TypeInsnNode(192, argType.getInternalName()));
                        }
                        if (argType.getSize() == 2) {
                            insns.add(new InsnNode(93));
                            insns.add(new InsnNode(88));
                        } else {
                            insns.add(new InsnNode(95));
                        }
                    }
                    insns.add(new InsnNode(87));
                    int boundParamIndex = z ? 1 : 0;
                    for (Type boundParamType : typeArr2) {
                        insns.add(new VarInsnNode(boundParamType.getOpcode(21), boundParamIndex));
                        boundParamIndex += boundParamType.getSize();
                    }
                };
                add(contents.generate(paramArrayIndex, loadArgs));
                if (returnType == Type.VOID_TYPE) {
                    add(new InsnNode(1));
                    add(new TypeInsnNode(192, "java/lang/Void"));
                } else if (ASMUtils.isPrimitive(returnType)) {
                    add(new MethodInsnNode(184, Bytecode.getBoxingType(returnType), "valueOf", Bytecode.generateDescriptor(Type.getObjectType(Bytecode.getBoxingType(returnType)), new Type[]{returnType}), false));
                }
                add(new InsnNode(176));
            }
        };
        classNode.methods.add(method);
        return new Handle(virtual ? 7 : 6, classNode.name, method.name, method.desc, (classNode.access & 512) != 0);
    }
}
