package com.rain.arcane_convergence.mixin;

import com.Polarice3.Goety.common.listeners.SoulTakenListener;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/SoulTakenListenerMixin.class */
@Mixin({SoulTakenListener.class})
public class SoulTakenListenerMixin {
    @Inject(method = {"getSoulAmount"}, at = {@At("RETURN")}, cancellable = true, remap = false)
    private static void onGetSoulAmount(LivingEntity victim, CallbackInfoReturnable<Integer> cir) {
        if (victim instanceof Player) {
            Player player = (Player) victim;
            IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse((Object) null);
            if (manaCap != null) {
                cir.setReturnValue(Integer.valueOf((int) manaCap.getCurrentMana()));
            }
        }
    }
}
