package com.rain.arcane_convergence;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:com/rain/arcane_convergence/ArcaneConvergence.class */
@Mod(ArcaneConvergence.MODID)
public class ArcaneConvergence {
    public static final String MODID = "arcane_convergence";
    public static final Logger LOGGER = LogManager.getLogger();

    public ArcaneConvergence() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, com.rain.arcane_convergence.config.ModConfig.SPEC);
    }
}
