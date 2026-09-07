package com.rain.arcane_convergence;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/MixinConfigPlugin.class */
public class MixinConfigPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> {
        return true;
    };
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of("com.rain.arcane_convergence.mixin.AttributeAffixMixin", () -> {
        return Boolean.valueOf((LoadingModList.get().getModFileById("irons_spellbooks") == null || LoadingModList.get().getModFileById("apotheosis") == null) ? false : true);
    }, "com.rain.arcane_convergence.mixin.GemMixin", () -> {
        return Boolean.valueOf((LoadingModList.get().getModFileById("irons_spellbooks") == null || LoadingModList.get().getModFileById("apotheosis") == null) ? false : true);
    }, "com.rain.arcane_convergence.mixin.ModifierInstMixin", () -> {
        return Boolean.valueOf((LoadingModList.get().getModFileById("irons_spellbooks") == null || LoadingModList.get().getModFileById("apotheosis") == null) ? false : true);
    }, "com.rain.arcane_convergence.mixin.SEHelperMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    }, "com.rain.arcane_convergence.mixin.AllSpellMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    }, "com.rain.arcane_convergence.mixin.IDarkWandMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    }, "com.rain.arcane_convergence.mixin.SoulTakenListenerMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    }, "com.rain.arcane_convergence.mixin.SoulEnergyGuiMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    }, "com.rain.arcane_convergence.mixin.SEImpMixin", () -> {
        return Boolean.valueOf(LoadingModList.get().getModFileById("goety") != null);
    });

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean shouldApply = CONDITIONS.getOrDefault(mixinClassName, TRUE).get().booleanValue();
        return shouldApply;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
