package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.rain.arcane_convergence.MagicDataPlayer;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/MagicDataMixin.class */
@Mixin({MagicData.class})
public class MagicDataMixin implements MagicDataPlayer {

    @Shadow
    private ServerPlayer serverPlayer;

    @Override // com.rain.arcane_convergence.MagicDataPlayer
    public ServerPlayer getPlayer() {
        return this.serverPlayer;
    }

    @WrapMethod(method = {"getMana"}, remap = false)
    public float getMana(Operation<Float> original) {
        if (getPlayer() == null) {
            return original.call(new Object[0]).floatValue();
        }
        IManaCap manaCap = (IManaCap) getPlayer().getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            return (float) manaCap.getCurrentMana();
        }
        return original.call(new Object[0]).floatValue();
    }

    @WrapMethod(method = {"setMana"}, remap = false)
    public void setMana(float mana, Operation<Void> original) {
        if (getPlayer() == null) {
            original.call(Float.valueOf(mana));
            return;
        }
        IManaCap manaCap = (IManaCap) getPlayer().getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            manaCap.setMana(mana);
        }
        original.call(Float.valueOf(mana));
    }
}
