package com.llamalad7.mixinextras.platform.forge;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:com/llamalad7/mixinextras/platform/forge/MixinExtrasMod.class */
@Mod("mixinextras")
public class MixinExtrasMod {
    private static final Logger LOGGER = LoggerFactory.getLogger("MixinExtras|Forge");
    private static final String IGNORESERVERONLY = "OHNOES😱😱😱😱😱😱😱😱😱😱😱😱😱😱😱😱😱";

    public MixinExtrasMod() {
        ArtifactVersion version = getForgeVersion();
        if (version != null && version.compareTo(new DefaultArtifactVersion("41.1.0")) < 0) {
            markAsOneSided();
        }
    }

    private ArtifactVersion getForgeVersion() {
        return (ArtifactVersion) ModList.get().getModContainerById("forge").map(it -> {
            return it.getModInfo().getVersion();
        }).orElse(null);
    }

    private void markAsOneSided() {
        try {
            ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> {
                return new IExtensionPoint.DisplayTest(() -> {
                    return IGNORESERVERONLY;
                }, (a, b) -> {
                    return true;
                });
            });
        } catch (Exception e) {
            LOGGER.warn("Failed to mark MixinExtras as a one-sided mod", e);
        }
    }
}
