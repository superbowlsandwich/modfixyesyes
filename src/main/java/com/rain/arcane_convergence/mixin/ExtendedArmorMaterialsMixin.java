package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorMaterials;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/ExtendedArmorMaterialsMixin.class */
@Mixin({ExtendedArmorMaterials.class})
public class ExtendedArmorMaterialsMixin {
    @WrapMethod(method = {"getAdditionalAttributes"}, remap = false)
    private Map<Attribute, AttributeModifier> onGetAdditionalAttributes(Operation<Map<Attribute, AttributeModifier>> original) {
        Map<Attribute, AttributeModifier> originalAttributes = original.call(new Object[0]);
        Map<Attribute, AttributeModifier> modifiedAttributes = new HashMap<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : originalAttributes.entrySet()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            double amount = modifier.m_22218_();
            if (attribute == AttributeRegistry.MAX_MANA.get()) {
                modifiedAttributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(modifier.m_22209_(), modifier.m_22214_(), amount, modifier.m_22217_()));
            } else {
                modifiedAttributes.put(attribute, modifier);
            }
        }
        return modifiedAttributes;
    }
}
