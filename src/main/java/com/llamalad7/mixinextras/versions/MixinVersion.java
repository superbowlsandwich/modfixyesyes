package com.llamalad7.mixinextras.versions;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.util.VersionNumber;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/versions/MixinVersion.class */
public abstract class MixinVersion {
    private static final List<String> VERSIONS = Arrays.asList("0.8.7", "0.8.4", "0.8.3", "0.8");
    private static final MixinVersion INSTANCE;

    public abstract RuntimeException makeInvalidInjectionException(InjectionInfo injectionInfo, String str);

    public abstract IMixinContext getMixin(InjectionInfo injectionInfo);

    public abstract LocalVariableDiscriminator.Context makeLvtContext(InjectionInfo injectionInfo, Type type, boolean z, Target target, AbstractInsnNode abstractInsnNode);

    public abstract void preInject(InjectionInfo injectionInfo);

    public abstract AnnotationNode getAnnotation(InjectionInfo injectionInfo);

    public abstract int getOrder(InjectionInfo injectionInfo);

    static {
        VersionNumber currentVersion = VersionNumber.parse(MixinEnvironment.getCurrentEnvironment().getVersion());
        MixinVersion current = null;
        for (String version : VERSIONS) {
            if (VersionNumber.parse(version).compareTo(currentVersion) <= 0) {
                try {
                    Class<?> implClass = Class.forName(MixinVersion.class.getPackage().getName() + ".MixinVersionImpl_v" + version.replace('.', '_'));
                    current = (MixinVersion) implClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                    break;
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        INSTANCE = current;
    }

    public static MixinVersion getInstance() {
        return INSTANCE;
    }
}
