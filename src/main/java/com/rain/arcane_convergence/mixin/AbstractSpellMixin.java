package com.rain.arcane_convergence.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.rain.arcane_convergence.config.ModConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/AbstractSpellMixin.class */
@Mixin({AbstractSpell.class})
public class AbstractSpellMixin {
    @WrapMethod(method = {"getManaCost"}, remap = false)
    public int getManaCost(int level, Operation<Integer> original) {
        if (ModConfig.IRON_SPELLS_MANA_MULTIPLIER != null) {
            return Mth.m_14143_((int) (((double) original.call(Integer.valueOf(level)).intValue()) * ((Double) ModConfig.IRON_SPELLS_MANA_MULTIPLIER.get()).doubleValue()));
        }
        return Mth.m_14107_(((double) original.call(Integer.valueOf(level)).intValue()) * 1.5d);
    }
}
