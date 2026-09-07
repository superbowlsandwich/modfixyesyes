package com.llamalad7.mixinextras;

import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.service.MixinExtrasVersion;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/MixinExtrasBootstrap.class */
public class MixinExtrasBootstrap {
    private static boolean initialized = false;

    @Deprecated
    public static String getVersion() {
        return MixinExtrasVersion.LATEST.toString();
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        MixinExtrasService.setup();
    }
}
