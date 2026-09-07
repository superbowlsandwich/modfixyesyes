package com.rain.arcane_convergence.mixin;

import com.Polarice3.Goety.utils.SEHelper;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/SEHelperMixin.class */
@Mixin({SEHelper.class})
public class SEHelperMixin {
    @WrapMethod(method = {"getSESouls"}, remap = false)
    private static int getSESouls(Player player, Operation<Integer> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            return original.call(player).intValue();
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            return (int) manaCap.getCurrentMana();
        }
        return original.call(player).intValue();
    }

    @WrapMethod(method = {"setSESouls"}, remap = false)
    private static void setSESouls(Player player, int souls, Operation<Void> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            original.call(player, Integer.valueOf(souls));
            return;
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            manaCap.setMana(souls);
        }
        original.call(player, Integer.valueOf(souls));
    }

    @WrapMethod(method = {"setSoulsAmount"}, remap = false)
    private static void setSoulsAmount(Player player, int souls, Operation<Void> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            original.call(player, Integer.valueOf(souls));
            return;
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            manaCap.setMana(souls);
        }
        original.call(player, Integer.valueOf(souls));
    }

    @WrapMethod(method = {"getSoulAmountInt"}, remap = false)
    private static int getSoulAmountInt(Player player, Operation<Integer> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            return original.call(player).intValue();
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            return (int) manaCap.getCurrentMana();
        }
        return original.call(player).intValue();
    }

    @Inject(method = {"getSoulGiven"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    private static void getSoulGiven(LivingEntity victim, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @WrapMethod(method = {"decreaseSouls"}, remap = false)
    private static void decreaseSouls(Player player, int souls, Operation<Void> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            original.call(player, Integer.valueOf(souls));
            return;
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            manaCap.setMana(manaCap.getCurrentMana() - ((double) souls));
        }
        original.call(player, Integer.valueOf(souls));
    }

    @WrapMethod(method = {"increaseSouls"}, remap = false)
    private static void increaseSouls(Player player, int souls, Operation<Void> original) {
        // Only run custom logic on server players, otherwise use original
        if (!(player instanceof ServerPlayer)) {
            original.call(player, Integer.valueOf(souls));
            return;
        }
        IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse(null);
        if (manaCap != null) {
            manaCap.setMana(manaCap.getCurrentMana() + ((double) souls));
        }
        original.call(player, Integer.valueOf(souls));
    }
}
