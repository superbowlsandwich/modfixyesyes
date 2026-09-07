package com.llamalad7.mixinextras.injector;

import com.llamalad7.mixinextras.lib.apache.commons.ClassUtils;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import com.llamalad7.mixinextras.utils.ProxyUtils;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/LateApplyingInjectorInfo.class */
public interface LateApplyingInjectorInfo {
    void lateInject();

    void latePostInject();

    void wrap(LateApplyingInjectorInfo lateApplyingInjectorInfo);

    String getLateInjectionType();

    @Deprecated
    default void lateApply() {
        lateInject();
        MixinExtrasLogger logger = MixinExtrasLogger.get("Sugar");
        logger.warn("Skipping post injection checks for {} since it is from 0.2.0-beta.1 and cannot be saved", this);
    }

    static boolean wrap(Object inner, LateApplyingInjectorInfo outer) {
        Class<?> theirInterface = ClassUtils.getAllInterfaces(inner.getClass()).stream().filter(it -> {
            return it.getName().endsWith(".LateApplyingInjectorInfo");
        }).findFirst().orElse(null);
        if (theirInterface == null || !MixinExtrasService.getInstance().isClassOwned(theirInterface.getName())) {
            return false;
        }
        try {
            inner.getClass().getMethod("wrap", theirInterface).invoke(inner, ProxyUtils.getProxy(outer, theirInterface));
            return true;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to wrap InjectionInfo: ", e);
        }
    }
}
