package com.llamalad7.mixinextras.utils;

import com.llamalad7.mixinextras.lib.apache.commons.ClassUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/ProxyUtils.class */
public class ProxyUtils {
    public static <T> T getProxy(Object obj, Class<T> cls) {
        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        }
        String simpleName = cls.getSimpleName();
        if (ClassUtils.getAllInterfaces(obj.getClass()).stream().anyMatch(it -> {
            return it.getName().endsWith('.' + simpleName);
        })) {
            return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, (proxy, method, args) -> {
                Method original = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
                original.setAccessible(true);
                return original.invoke(obj, args);
            });
        }
        throw new UnsupportedOperationException(String.format("Cannot get a %s instance from %s", simpleName, obj));
    }
}
