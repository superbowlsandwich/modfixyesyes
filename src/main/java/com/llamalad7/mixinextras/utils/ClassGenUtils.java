package com.llamalad7.mixinextras.utils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/ClassGenUtils.class */
public class ClassGenUtils {
    private static final Definer DEFINER;
    private static final Map<String, byte[]> DEFINITIONS = new HashMap();

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/ClassGenUtils$Definer.class */
    @FunctionalInterface
    private interface Definer {
        void define(String str, byte[] bArr, MethodHandles.Lookup lookup) throws Throwable;
    }

    static {
        Definer theDefiner;
        try {
            Method defineClass = Unsafe.class.getMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class);
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            theDefiner = (name, bytes, scope) -> {
                defineClass.invoke(unsafe, name, bytes, 0, Integer.valueOf(bytes.length), scope.lookupClass().getClassLoader(), scope.lookupClass().getProtectionDomain());
            };
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException e1) {
            try {
                Method defineClass2 = MethodHandles.Lookup.class.getMethod("defineClass", byte[].class);
                theDefiner = (name2, bytes2, scope2) -> {
                    defineClass2.invoke(scope2, bytes2);
                };
            } catch (NoSuchMethodException e2) {
                RuntimeException e = new RuntimeException("Could not resolve class definer! Please report to LlamaLad7.");
                e.addSuppressed(e1);
                e.addSuppressed(e2);
                throw e;
            }
        }
        DEFINER = theDefiner;
    }

    public static void defineClass(ClassNode node, MethodHandles.Lookup scope) {
        ClassWriter writer = new ClassWriter(2);
        node.accept(writer);
        byte[] bytes = writer.toByteArray();
        String name = node.name.replace('/', '.');
        try {
            DEFINER.define(name, bytes, scope);
            DEFINITIONS.put(name, bytes);
            MixinInternals.registerClassInfo(node);
            MixinInternals.getExtensions().export(MixinEnvironment.getCurrentEnvironment(), node.name, false, node);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Failed to define class %s from %s! Please report to LlamaLad7!", node.name, scope), e);
        }
    }

    public static Map<String, byte[]> getDefinitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }
}
