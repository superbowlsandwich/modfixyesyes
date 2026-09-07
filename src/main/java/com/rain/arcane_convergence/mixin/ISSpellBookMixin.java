package com.rain.arcane_convergence.mixin;

import com.hollingsworth.arsnouveau.api.client.IDisplayMana;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/ISSpellBookMixin.class */
@Mixin({SpellBook.class})
public class ISSpellBookMixin implements IDisplayMana {
    public boolean shouldDisplay(ItemStack stack) {
        return true;
    }
}
