package com.rain.arcane_convergence.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/MagicManagerMixin.class */
@Mixin({MagicManager.class})
public class MagicManagerMixin {
    @Inject(method = {"regenPlayerMana"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    public void regenPlayerMana(ServerPlayer serverPlayer, MagicData playerMagicData, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
