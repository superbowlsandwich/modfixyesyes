package com.rain.arcane_convergence.event;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.rain.arcane_convergence.ArcaneConvergence;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import java.util.List;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/event/CommonForgeEvents.class */
@Mod.EventBusSubscriber(modid = ArcaneConvergence.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {
    @SubscribeEvent
    public static void onAttributeModify(ItemAttributeModifierEvent event) {
        if (event.getModifiers().containsKey(AttributeRegistry.MAX_MANA.get())) {
            List<AttributeModifier> modifier = event.getModifiers().get((Attribute) AttributeRegistry.MAX_MANA.get()).stream().toList();
            modifier.forEach(attributeModifier -> {
                event.addModifier((Attribute) PerkAttributes.MAX_MANA.get(), attributeModifier);
            });
            event.removeAttribute((Attribute) AttributeRegistry.MAX_MANA.get());
        }
        if (event.getModifiers().containsKey(AttributeRegistry.MANA_REGEN.get())) {
            List<AttributeModifier> modifier2 = event.getModifiers().get((Attribute) AttributeRegistry.MANA_REGEN.get()).stream().toList();
            modifier2.forEach(attributeModifier2 -> {
                event.addModifier((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(attributeModifier2.m_22209_(), attributeModifier2.m_22214_(), attributeModifier2.m_22218_() * 0.6d, attributeModifier2.m_22217_()));
            });
            event.removeAttribute((Attribute) AttributeRegistry.MANA_REGEN.get());
        }
    }
}
