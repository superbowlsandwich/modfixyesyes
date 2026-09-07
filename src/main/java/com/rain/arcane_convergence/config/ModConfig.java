package com.rain.arcane_convergence.config;

import com.rain.arcane_convergence.ArcaneConvergence;
import net.minecraftforge.common.ForgeConfigSpec;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/config/ModConfig.class */
public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.DoubleValue IRON_SPELLS_MANA_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue GOETY_SOUL_MULTIPLIER;
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push(ArcaneConvergence.MODID);
        BUILDER.push("multipliers");
        IRON_SPELLS_MANA_MULTIPLIER = BUILDER.comment(new String[]{"Iron's Spells Mana Cost Multiplier (0 - 100000000.0)", "铁魔法的法力消耗倍率(0 - 100000000.0)"}).defineInRange("ironSpellsManaMultiplier", 1.0d, 0.0d, 1.0E8d);
        GOETY_SOUL_MULTIPLIER = BUILDER.comment(new String[]{"Goety Soul Cost Multiplier (0 - 100000000.0)", "诡厄巫法的灵魂消耗倍率(0 - 100000000.0)"}).defineInRange("goetySoulMultiplier", 1.0d, 0.0d, 1.0E8d);
        BUILDER.pop();
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
