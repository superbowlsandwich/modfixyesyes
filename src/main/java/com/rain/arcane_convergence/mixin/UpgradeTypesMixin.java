package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.item.armor.UpgradeTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/UpgradeTypesMixin.class */
@Mixin({UpgradeTypes.class})
public class UpgradeTypesMixin {
    @Inject(method = {"getAttribute"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    private void modifyManaAttribute(CallbackInfoReturnable<Attribute> cir) {
        UpgradeTypes self = (UpgradeTypes) this;
        if (self == UpgradeTypes.MANA) {
            cir.setReturnValue((Attribute) PerkAttributes.MAX_MANA.get());
        }
    }
}
