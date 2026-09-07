package com.llamalad7.mixinextras.sugar.impl;

import com.llamalad7.mixinextras.injector.StackExtension;
import com.llamalad7.mixinextras.sugar.impl.ref.LocalRefClassGenerator;
import com.llamalad7.mixinextras.sugar.impl.ref.LocalRefUtils;
import com.llamalad7.mixinextras.utils.CompatibilityHelper;
import com.llamalad7.mixinextras.utils.Decorations;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.modify.InvalidImplicitDiscriminatorException;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.SignaturePrinter;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/LocalSugarApplicator.class */
class LocalSugarApplicator extends SugarApplicator {
    private final boolean isArgsOnly;
    private final Type targetLocalType;
    private final boolean isMutable;

    LocalSugarApplicator(InjectionInfo info, SugarParameter parameter) {
        super(info, parameter);
        this.targetLocalType = LocalRefUtils.getTargetType(this.paramType, this.paramGeneric);
        this.isMutable = this.targetLocalType != this.paramType;
        this.isArgsOnly = ((Boolean) Annotations.getValue(this.sugar, "argsOnly", false)).booleanValue();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void validate(Target target, InjectionNodes.InjectionNode node) throws MixinException {
        LocalVariableDiscriminator discriminator = LocalVariableDiscriminator.parse(this.sugar);
        LocalVariableDiscriminator.Context context = getOrCreateLocalContext(target, node);
        if (discriminator.printLVT()) {
            printLocals(target, node.getCurrentTarget(), context, discriminator);
            this.info.addCallbackInvocation(this.info.getMethod());
            throw new SugarApplicationException("Application aborted because locals are being printed instead.");
        }
        try {
            if (discriminator.findLocal(context) < 0) {
                throw new SugarApplicationException("Unable to find matching local!");
            }
        } catch (InvalidImplicitDiscriminatorException e) {
            throw new SugarApplicationException("Invalid implicit variable discriminator: ", e);
        }
    }

    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void prepare(Target target, InjectionNodes.InjectionNode node) {
        getOrCreateLocalContext(target, node);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    void inject(Target target, InjectionNodes.InjectionNode node, StackExtension stack) throws MixinException {
        LocalVariableDiscriminator discriminator = LocalVariableDiscriminator.parse(this.sugar);
        LocalVariableDiscriminator.Context context = (LocalVariableDiscriminator.Context) node.getDecoration(getLocalContextKey());
        int index = discriminator.findLocal(context);
        if (index < 0) {
            throw new SugarApplicationException("Failed to match a local, this should have been caught during validation.");
        }
        if (this.isMutable) {
            initAndLoadLocalRef(target, node, index, stack);
        } else {
            stack.extra(this.targetLocalType.getSize());
            target.insns.insertBefore(node.getCurrentTarget(), new VarInsnNode(this.targetLocalType.getOpcode(21), index));
        }
    }

    @Override // com.llamalad7.mixinextras.sugar.impl.SugarApplicator
    int postProcessingPriority() {
        return 1000;
    }

    private void initAndLoadLocalRef(Target target, InjectionNodes.InjectionNode node, int index, StackExtension stack) {
        String refName = LocalRefClassGenerator.getForType(this.targetLocalType);
        int refIndex = getOrCreateRef(target, node, index, refName, stack);
        stack.extra(1);
        target.insns.insertBefore(node.getCurrentTarget(), new VarInsnNode(25, refIndex));
    }

    private int getOrCreateRef(Target target, InjectionNodes.InjectionNode node, int index, String refImpl, StackExtension stack) {
        Map<Integer, Integer> refIndices = (Map) node.getDecoration(Decorations.LOCAL_REF_MAP);
        if (refIndices == null) {
            refIndices = new HashMap<>();
            node.decorate(Decorations.LOCAL_REF_MAP, refIndices);
        }
        if (refIndices.containsKey(Integer.valueOf(index))) {
            return refIndices.get(Integer.valueOf(index)).intValue();
        }
        int refIndex = target.allocateLocal();
        target.addLocalVariable(refIndex, "ref" + refIndex, 'L' + refImpl + ';');
        InsnList construction = new InsnList();
        LocalRefUtils.generateNew(construction, this.targetLocalType);
        construction.add(new VarInsnNode(58, refIndex));
        target.insertBefore(node, construction);
        SugarPostProcessingExtension.enqueuePostProcessing(this, () -> {
            InsnList initialization = new InsnList();
            initialization.add(new VarInsnNode(25, refIndex));
            initialization.add(new VarInsnNode(this.targetLocalType.getOpcode(21), index));
            LocalRefUtils.generateInitialization(initialization, this.targetLocalType);
            target.insertBefore(node, initialization);
            InsnList after = new InsnList();
            after.add(new VarInsnNode(25, refIndex));
            LocalRefUtils.generateDisposal(after, this.targetLocalType);
            after.add(new VarInsnNode(this.targetLocalType.getOpcode(54), index));
            target.insns.insert(node.getCurrentTarget(), after);
        });
        stack.extra(this.targetLocalType.getSize() + 1);
        refIndices.put(Integer.valueOf(index), Integer.valueOf(refIndex));
        return refIndex;
    }

    private LocalVariableDiscriminator.Context getOrCreateLocalContext(Target target, InjectionNodes.InjectionNode node) {
        String decorationKey = getLocalContextKey();
        if (node.hasDecoration(decorationKey)) {
            return (LocalVariableDiscriminator.Context) node.getDecoration(decorationKey);
        }
        LocalVariableDiscriminator.Context context = CompatibilityHelper.makeLvtContext(this.info, this.targetLocalType, this.isArgsOnly, target, node.getCurrentTarget());
        node.decorate(decorationKey, context);
        return context;
    }

    private String getLocalContextKey() {
        Object[] objArr = new Object[2];
        objArr[0] = this.targetLocalType;
        objArr[1] = this.isArgsOnly ? "argsOnly" : "fullFrame";
        return String.format("mixinextras_persistent_localSugarContext(%s,%s)", objArr);
    }

    private void printLocals(Target target, AbstractInsnNode node, LocalVariableDiscriminator.Context context, LocalVariableDiscriminator discriminator) {
        int baseArgIndex = target.isStatic ? 0 : 1;
        new PrettyPrinter().kvWidth(20).kv("Target Class", target.classNode.name.replace('/', '.')).kv("Target Method", target.method.name).kv("Capture Type", SignaturePrinter.getTypeName(this.targetLocalType, false)).kv("Instruction", "[%d] %s %s", new Object[]{Integer.valueOf(target.insns.indexOf(node)), node.getClass().getSimpleName(), Bytecode.getOpcodeName(node.getOpcode())}).hr().kv("Match mode", isImplicit(discriminator, baseArgIndex) ? "IMPLICIT (match single)" : "EXPLICIT (match by criteria)").kv("Match ordinal", discriminator.getOrdinal() < 0 ? "any" : Integer.valueOf(discriminator.getOrdinal())).kv("Match index", discriminator.getIndex() < baseArgIndex ? "any" : Integer.valueOf(discriminator.getIndex())).kv("Match name(s)", discriminator.hasNames() ? discriminator.getNames() : "any").kv("Args only", Boolean.valueOf(this.isArgsOnly)).hr().add(context).print(System.err);
    }

    private boolean isImplicit(LocalVariableDiscriminator discriminator, int baseArgIndex) {
        return discriminator.getOrdinal() < 0 && discriminator.getIndex() < baseArgIndex && discriminator.getNames().isEmpty();
    }
}
