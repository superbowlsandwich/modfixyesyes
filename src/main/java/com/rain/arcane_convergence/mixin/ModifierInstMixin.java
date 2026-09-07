package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.placebo.util.StepFunction;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/ModifierInstMixin.class */
@Mixin({AttributeAffix.ModifierInst.class})
public class ModifierInstMixin {

    @Shadow
    @Mutable
    @Final
    private Attribute attr;

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void onInit(Attribute attra, AttributeModifier.Operation op, StepFunction valueFactory, CallbackInfo ci) {
        if (this.attr == AttributeRegistry.MAX_MANA.get()) {
            this.attr = (Attribute) PerkAttributes.MAX_MANA.get();
        }
        if (this.attr == AttributeRegistry.MANA_REGEN.get()) {
            this.attr = (Attribute) PerkAttributes.MANA_REGEN_BONUS.get();
        }
    }
}
