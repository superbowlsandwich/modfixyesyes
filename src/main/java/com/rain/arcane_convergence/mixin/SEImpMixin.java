package com.rain.arcane_convergence.mixin;

import com.Polarice3.Goety.common.capabilities.soulenergy.SEImp;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/SEImpMixin.class */
@Mixin({SEImp.class})
public class SEImpMixin {
    private List<ServerPlayer> getServerPlayers() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server.m_6846_().m_11314_();
    }

    @WrapMethod(method = {"getSoulEnergy"}, remap = false)
    public int getMana(Operation<Integer> original) {
        List<ServerPlayer> players = getServerPlayers();
        if (players.isEmpty()) {
            return original.call(new Object[0]).intValue();
        }
        for (ServerPlayer player : players) {
            IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse((Object) null);
            if (manaCap != null) {
                return (int) manaCap.getCurrentMana();
            }
        }
        return original.call(new Object[0]).intValue();
    }

    @WrapMethod(method = {"setSoulEnergy"}, remap = false)
    public void setMana(int mana, Operation<Void> original) {
        List<ServerPlayer> players = getServerPlayers();
        if (players.isEmpty()) {
            original.call(Integer.valueOf(mana));
            return;
        }
        for (ServerPlayer player : players) {
            IManaCap manaCap = (IManaCap) player.getCapability(CapabilityRegistry.MANA_CAPABILITY).orElse((Object) null);
            if (manaCap != null) {
                manaCap.setMana(mana);
            }
        }
        original.call(Integer.valueOf(mana));
    }

    @Inject(method = {"getSEActive"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    private void getSEActive(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
