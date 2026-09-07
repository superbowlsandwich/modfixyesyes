package com.llamalad7.mixinextras.transformer;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.llamalad7.mixinextras.sugar.impl.SugarMixinTransformer;
import com.llamalad7.mixinextras.utils.MixinInternals;
import com.llamalad7.mixinextras.wrapper.factory.FactoryRedirectWrapperMixinTransformer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/transformer/MixinTransformerExtension.class */
public class MixinTransformerExtension implements IExtension {
    private final Set<ClassNode> preparedMixins = Collections.newSetFromMap(new WeakHashMap());
    private final List<MixinTransformer> transformers = Arrays.asList(new FactoryRedirectWrapperMixinTransformer(), new SugarMixinTransformer());

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
        for (Pair<IMixinInfo, ClassNode> pair : MixinInternals.getMixinsFor(context)) {
            IMixinInfo info = pair.getLeft();
            ClassNode node = pair.getRight();
            if (!this.preparedMixins.contains(node)) {
                for (MixinTransformer transformer : this.transformers) {
                    transformer.transform(info, node);
                }
                this.preparedMixins.add(node);
            }
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
