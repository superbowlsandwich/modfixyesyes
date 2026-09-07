package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/AttributeAffixMixin.class */
@Mixin({AttributeAffix.class})
public class AttributeAffixMixin {

    @Shadow
    @Mutable
    @Final
    protected Attribute attribute;

    @Inject(method = {"addModifiers"}, at = {@At("HEAD")}, remap = false)
    public void addModifiers(ItemStack stack, LootRarity rarity, float level, EquipmentSlot type, BiConsumer<Attribute, AttributeModifier> map, CallbackInfo ci) {
        if (this.attribute == AttributeRegistry.MAX_MANA.get()) {
            this.attribute = (Attribute) PerkAttributes.MAX_MANA.get();
        }
        if (this.attribute == AttributeRegistry.MANA_REGEN.get()) {
            this.attribute = (Attribute) PerkAttributes.MANA_REGEN_BONUS.get();
        }
    }
}
