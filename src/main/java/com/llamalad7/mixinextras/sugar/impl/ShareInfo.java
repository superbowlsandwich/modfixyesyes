package com.llamalad7.mixinextras.sugar.impl;

import com.llamalad7.mixinextras.injector.StackExtension;
import com.llamalad7.mixinextras.sugar.impl.ref.LocalRefUtils;
import com.llamalad7.mixinextras.utils.TargetDecorations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.util.Annotations;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/ShareInfo.class */
public class ShareInfo {
    private int lvtIndex;
    private final ShareType shareType;
    private final Collection<AbstractInsnNode> initialization = new ArrayList();

    private ShareInfo(int lvtIndex, Type innerType) {
        this.lvtIndex = lvtIndex;
        this.shareType = new ShareType(innerType);
    }

    public int getLvtIndex() {
        return this.lvtIndex;
    }

    public void setLvtIndex(int lvtIndex) {
        this.lvtIndex = lvtIndex;
    }

    public ShareType getShareType() {
        return this.shareType;
    }

    public void addToLvt(Target target) {
        this.shareType.addToLvt(target, this.lvtIndex);
    }

    public InsnList initialize() {
        InsnList init = this.shareType.initialize(this.lvtIndex);
        this.initialization.addAll(Arrays.asList(init.toArray()));
        return init;
    }

    public AbstractInsnNode load() {
        return new VarInsnNode(25, this.lvtIndex);
    }

    public void stripInitializerFrom(MethodNode method) {
        Collection<AbstractInsnNode> collection = this.initialization;
        InsnList insnList = method.instructions;
        Objects.requireNonNull(insnList);
        collection.forEach(insnList::remove);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    public static ShareInfo getOrCreate(Target target, AnnotationNode shareAnnotation, Type paramType, IMixinInfo mixin, StackExtension stack) throws MixinException {
        if (!SugarApplicator.isSugar(shareAnnotation.desc) || !shareAnnotation.desc.endsWith("Share;")) {
            return null;
        }
        Type innerType = getInnerType(paramType);
        Map<ShareId, ShareInfo> infos = (Map) TargetDecorations.getOrPut(target, "ShareSugar_Infos", HashMap::new);
        ShareId id = getId(shareAnnotation, mixin);
        ShareInfo shareInfo = infos.get(id);
        if (shareInfo == null) {
            shareInfo = new ShareInfo(target.allocateLocal(), innerType);
            infos.put(id, shareInfo);
            shareInfo.addToLvt(target);
            target.insns.insert(shareInfo.initialize());
            if (stack != null) {
                stack.ensureAtLeast(innerType.getSize() + 2);
            }
        } else if (!innerType.equals(shareInfo.shareType.getInnerType())) {
            throw new SugarApplicationException(String.format("Share id %s in %s was requested for different types %s and %s!", id, target, innerType, shareInfo.shareType.getInnerType()));
        }
        return shareInfo;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.spongepowered.asm.mixin.throwables.MixinException */
    private static Type getInnerType(Type paramType) throws MixinException {
        Type innerType = LocalRefUtils.getTargetType(paramType, Type.getType(Object.class));
        if (innerType == paramType) {
            throw new SugarApplicationException("@Share parameter must be some variation of LocalRef.");
        }
        return innerType;
    }

    private static ShareId getId(AnnotationNode shareAnnotation, IMixinInfo mixin) {
        return new ShareId((String) Annotations.getValue(shareAnnotation, "namespace", mixin.getClassName()), (String) Annotations.getValue(shareAnnotation));
    }

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/ShareInfo$ShareId.class */
    private static class ShareId {
        private final String namespace;
        private final String id;

        private ShareId(String namespace, String id) {
            this.namespace = namespace;
            this.id = id;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ShareId shareId = (ShareId) o;
            return Objects.equals(this.namespace, shareId.namespace) && Objects.equals(this.id, shareId.id);
        }

        public int hashCode() {
            return Objects.hash(this.namespace, this.id);
        }

        public String toString() {
            return this.namespace + ':' + this.id;
        }
    }
}
