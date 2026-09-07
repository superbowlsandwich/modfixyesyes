package com.llamalad7.mixinextras.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/MixinExtrasLogger.class */
public interface MixinExtrasLogger {
    void warn(String str, Object... objArr);

    void info(String str, Object... objArr);

    void debug(String str, Object... objArr);

    void error(String str, Throwable th);

    static MixinExtrasLogger get(String name) {
        Object impl;
        try {
            IMixinService service = MixinService.getService();
            Method getLogger = service.getClass().getMethod("getLogger", String.class);
            impl = getLogger.invoke(service, "MixinExtras|" + name);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
            try {
                impl = Class.forName("org.apache.logging.log4j.LogManager").getMethod("getLogger", String.class).invoke(null, name);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                RuntimeException e = new IllegalStateException("Could not get logger! Please inform LlamaLad7!");
                e.addSuppressed(e1);
                e.addSuppressed(e2);
                throw e;
            }
        }
        Object finalImpl = impl;
        return (MixinExtrasLogger) Proxy.newProxyInstance(MixinExtrasLogger.class.getClassLoader(), new Class[]{MixinExtrasLogger.class}, (proxy, method, args) -> {
            return finalImpl.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(finalImpl, args);
        });
    }
}
