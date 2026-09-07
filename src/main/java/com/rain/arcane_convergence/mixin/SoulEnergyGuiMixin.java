package com.rain.arcane_convergence.mixin;

import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/mixin/SoulEnergyGuiMixin.class */
@Mixin({SoulEnergyGui.class})
public class SoulEnergyGuiMixin {
    @Inject(method = {"drawHUD"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    private static void drawHUD(ForgeGui gui, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight, CallbackInfo ci) {
        ci.cancel();
    }
}
