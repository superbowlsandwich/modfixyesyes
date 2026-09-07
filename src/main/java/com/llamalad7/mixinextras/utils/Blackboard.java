package com.llamalad7.mixinextras.utils;

import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.MixinService;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/Blackboard.class */
public class Blackboard {
    private static final IGlobalPropertyService SERVICE = MixinService.getGlobalPropertyService();

    public static <T> T get(String str) {
        Object[] objArr = (Object[]) SERVICE.getProperty(SERVICE.resolveKey(str));
        if (objArr == null) {
            return null;
        }
        return (T) objArr[0];
    }

    public static void put(String key, Object value) {
        SERVICE.setProperty(SERVICE.resolveKey(key), new Object[1]);
        ((Object[]) SERVICE.getProperty(SERVICE.resolveKey(key)))[0] = value;
    }
}
