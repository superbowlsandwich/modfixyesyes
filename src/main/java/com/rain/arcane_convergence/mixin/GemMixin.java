package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.rain.arcane_convergence.AttributeBonusDuck;
import dev.shadowsoffire.apotheosis.adventure.affix.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.affix.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/GemMixin.class */
@Mixin({Gem.class})
public class GemMixin {
    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void onInit(int weight, float quality, Set<ResourceLocation> dimensions, Optional<LootRarity> minRarity, Optional<LootRarity> maxRarity, List<GemBonus> bonuses, boolean unique, Optional<Set<String>> stages, CallbackInfo ci) {
        Iterator<GemBonus> it = bonuses.iterator();
        while (it.hasNext()) {
            AttributeBonusDuck attributeBonusDuck = (GemBonus) it.next();
            if (attributeBonusDuck instanceof AttributeBonusDuck) {
                AttributeBonusDuck abonus = attributeBonusDuck;
                if (abonus.getAttribute() == AttributeRegistry.MAX_MANA.get()) {
                    abonus.setAttribute((Attribute) PerkAttributes.MAX_MANA.get());
                }
                if (abonus.getAttribute() == AttributeRegistry.MANA_REGEN.get()) {
                    abonus.setAttribute((Attribute) PerkAttributes.MANA_REGEN_BONUS.get());
                }
            }
        }
    }
}
