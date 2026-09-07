package com.rain.arcane_convergence.mixin;

import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.hollingsworth.arsnouveau.api.client.IDisplayMana;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/IDarkWandMixin.class */
@Mixin({DarkWand.class})
public class IDarkWandMixin implements IDisplayMana {
    public boolean shouldDisplay(ItemStack stack) {
        return true;
    }
}
