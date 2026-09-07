package com.llamalad7.mixinextras.injector.wrapmethod;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import com.llamalad7.mixinextras.sugar.impl.ShareInfo;
import com.llamalad7.mixinextras.utils.ASMUtils;
import com.llamalad7.mixinextras.utils.OperationUtils;
import com.llamalad7.mixinextras.utils.UniquenessHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeAnnotationNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodStage.class */
public abstract class WrapMethodStage {
    protected abstract MethodNode getVanillaMethod();

    public abstract MethodNode apply(ClassNode classNode, LinkedHashSet<ShareInfo> linkedHashSet);

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodStage$Vanilla.class */
    public static class Vanilla extends WrapMethodStage {
        private final MethodNode original;

        public Vanilla(MethodNode original) {
            this.original = original;
        }

        @Override // com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage
        protected MethodNode getVanillaMethod() {
            return this.original;
        }

        @Override // com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage
        public MethodNode apply(ClassNode targetClass, LinkedHashSet<ShareInfo> gatheredShares) {
            stripShareInitializers(gatheredShares);
            int shareStartIndex = Bytecode.getFirstNonArgLocalIndex(this.original);
            changeDesc(gatheredShares);
            fixLocals(shareStartIndex, new ArrayList(gatheredShares));
            return this.original;
        }

        private void stripShareInitializers(LinkedHashSet<ShareInfo> gatheredShares) {
            for (ShareInfo share : gatheredShares) {
                share.stripInitializerFrom(this.original);
            }
        }

        private void changeDesc(LinkedHashSet<ShareInfo> gatheredShares) {
            Type[] shareParams = (Type[]) gatheredShares.stream().map(it -> {
                return it.getShareType().getImplType();
            }).toArray(x$0 -> {
                return new Type[x$0];
            });
            Type[] params = (Type[]) ArrayUtils.addAll(Type.getArgumentTypes(this.original.desc), shareParams);
            Type returnType = Type.getReturnType(this.original.desc);
            this.original.desc = Type.getMethodDescriptor(returnType, params);
        }

        private void fixLocals(int shareStartIndex, List<ShareInfo> allShares) {
            if (allShares.isEmpty()) {
                return;
            }
            Map<Integer, Integer> oldToNewShares = (Map) IntStream.range(0, allShares.size()).boxed().collect(Collectors.toMap(i -> {
                return Integer.valueOf(((ShareInfo) allShares.get(i.intValue())).getLvtIndex());
            }, i2 -> {
                return Integer.valueOf(shareStartIndex + i2.intValue());
            }));
            for (ShareInfo share : allShares) {
                share.setLvtIndex(oldToNewShares.get(Integer.valueOf(share.getLvtIndex())).intValue());
            }
            IntUnaryOperator changeIndex = index -> {
                Integer newShare = (Integer) oldToNewShares.get(Integer.valueOf(index));
                if (newShare != null) {
                    return newShare.intValue();
                }
                if (index < shareStartIndex) {
                    return index;
                }
                return index + allShares.size();
            };
            for (VarInsnNode varInsnNode : this.original.instructions.toArray()) {
                if (varInsnNode instanceof VarInsnNode) {
                    VarInsnNode varNode = varInsnNode;
                    varNode.var = changeIndex.applyAsInt(varNode.var);
                } else if (varInsnNode instanceof IincInsnNode) {
                    IincInsnNode incNode = (IincInsnNode) varInsnNode;
                    incNode.var = changeIndex.applyAsInt(incNode.var);
                }
            }
            if (this.original.localVariables != null) {
                for (LocalVariableNode local : this.original.localVariables) {
                    local.index = changeIndex.applyAsInt(local.index);
                }
            }
        }
    }

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethodStage$Wrapper.class */
    public static class Wrapper extends WrapMethodStage {
        private final WrapMethodStage inner;
        private final MethodNode handler;
        private final Type operationType;
        private final List<ShareInfo> shares;
        private final boolean isStatic;

        public Wrapper(WrapMethodStage inner, MethodNode handler, Type operationType, List<ShareInfo> shares) {
            this.inner = inner;
            this.handler = handler;
            this.operationType = operationType;
            this.shares = shares;
            this.isStatic = Bytecode.isStatic(handler);
        }

        @Override // com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage
        protected MethodNode getVanillaMethod() {
            return this.inner.getVanillaMethod();
        }

        @Override // com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage
        public MethodNode apply(ClassNode targetClass, LinkedHashSet<ShareInfo> gatheredShares) {
            LinkedHashSet<ShareInfo> newShares = new LinkedHashSet<>(gatheredShares);
            List<ShareInfo> sharesToAllocate = new ArrayList<>();
            for (ShareInfo share : this.shares) {
                if (newShares.add(share)) {
                    sharesToAllocate.add(share);
                }
            }
            MethodNode vanilla = getVanillaMethod();
            Type[] operationArgs = Type.getArgumentTypes(vanilla.desc);
            Type returnType = Type.getReturnType(vanilla.desc);
            MethodNode wrapper = this.inner.apply(targetClass, newShares);
            MethodNode inner = move(targetClass, wrapper);
            fixDesc(wrapper, sharesToAllocate.size());
            InsnList insns = new InsnList();
            allocateShares(sharesToAllocate, insns);
            if (!this.isStatic) {
                insns.add(new VarInsnNode(25, 0));
            }
            Bytecode.loadArgs(operationArgs, insns, this.isStatic ? 0 : 1);
            if (!this.isStatic) {
                insns.add(new VarInsnNode(25, 0));
            }
            loadShares(newShares, insns);
            Type[] trailing = (Type[]) newShares.stream().map(it -> {
                return it.getShareType().getImplType();
            }).toArray(x$0 -> {
                return new Type[x$0];
            });
            OperationUtils.makeOperation(operationArgs, returnType, insns, !this.isStatic, trailing, targetClass, this.operationType, inner.name, (paramArrayIndex, loadArgs) -> {
                InsnList call = new InsnList();
                loadArgs.accept(call);
                call.add(ASMUtils.getInvokeInstruction(targetClass, inner));
                return call;
            });
            loadShares(this.shares, insns);
            insns.add(ASMUtils.getInvokeInstruction(targetClass, this.handler));
            insns.add(new InsnNode(returnType.getOpcode(172)));
            wrapper.instructions.add(insns);
            return wrapper;
        }

        private static void fixDesc(MethodNode wrapper, int shareCount) {
            Type[] argTypes = Type.getArgumentTypes(wrapper.desc);
            wrapper.desc = Type.getMethodDescriptor(Type.getReturnType(wrapper.desc), (Type[]) ArrayUtils.subarray(argTypes, 0, argTypes.length - shareCount));
        }

        private static void allocateShares(List<ShareInfo> sharesToAllocate, InsnList insns) {
            for (ShareInfo share : sharesToAllocate) {
                insns.add(share.initialize());
            }
        }

        private static void loadShares(Collection<ShareInfo> shares, InsnList insns) {
            for (ShareInfo share : shares) {
                insns.add(share.load());
            }
        }
    }

    protected static MethodNode move(ClassNode targetClass, MethodNode original) {
        MethodNode newMethod = new MethodNode(original.access, UniquenessHelper.getUniqueMethodName(targetClass, original.name + "$mixinextras$wrapped"), original.desc, (String) null, (String[]) null);
        Bytecode.setVisibility(newMethod, Bytecode.Visibility.PRIVATE);
        newMethod.instructions = original.instructions;
        newMethod.instructions.resetLabels();
        original.instructions = new InsnList();
        newMethod.tryCatchBlocks = original.tryCatchBlocks;
        original.tryCatchBlocks = null;
        newMethod.localVariables = original.localVariables;
        original.localVariables = null;
        stripLocalVariableReferences(original.visibleTypeAnnotations);
        stripLocalVariableReferences(original.invisibleTypeAnnotations);
        original.visibleLocalVariableAnnotations = null;
        original.invisibleLocalVariableAnnotations = null;
        targetClass.methods.add(newMethod);
        return newMethod;
    }

    private static void stripLocalVariableReferences(List<TypeAnnotationNode> nodes) {
        if (nodes == null) {
            return;
        }
        nodes.removeIf(it -> {
            return new TypeReference(it.typeRef).getSort() == 64;
        });
    }
}
