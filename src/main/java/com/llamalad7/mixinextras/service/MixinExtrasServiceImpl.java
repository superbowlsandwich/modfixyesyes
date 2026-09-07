package com.llamalad7.mixinextras.service;

import com.llamalad7.mixinextras.injector.LateInjectionApplicatorExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValueInjectionInfo;
import com.llamalad7.mixinextras.injector.ModifyReceiverInjectionInfo;
import com.llamalad7.mixinextras.injector.ModifyReturnValueInjectionInfo;
import com.llamalad7.mixinextras.injector.WrapWithConditionV1InjectionInfo;
import com.llamalad7.mixinextras.injector.v2.WrapWithConditionInjectionInfo;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodApplicatorExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodInjectionInfo;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperationInjectionInfo;
import com.llamalad7.mixinextras.lib.apache.commons.StringUtils;
import com.llamalad7.mixinextras.sugar.impl.SugarPostProcessingExtension;
import com.llamalad7.mixinextras.sugar.impl.SugarWrapperInjectionInfo;
import com.llamalad7.mixinextras.transformer.MixinTransformerExtension;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import com.llamalad7.mixinextras.utils.MixinInternals;
import com.llamalad7.mixinextras.wrapper.factory.FactoryRedirectWrapperInjectionInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/service/MixinExtrasServiceImpl.class */
public class MixinExtrasServiceImpl implements MixinExtrasService {
    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("Service");
    private final List<Versioned<String>> offeredPackages = new ArrayList();
    private final List<Versioned<IExtension>> offeredExtensions = new ArrayList();
    private final List<Versioned<Class<? extends InjectionInfo>>> offeredInjectors = new ArrayList();
    private final String ownPackage = StringUtils.substringBefore(getClass().getName(), ".service.");
    private final List<Versioned<String>> allPackages = new ArrayList(Collections.singletonList(new Versioned(getVersion(), this.ownPackage)));
    private final List<IExtension> ownExtensions = Arrays.asList(new MixinTransformerExtension(), new ServiceInitializationExtension(this), new LateInjectionApplicatorExtension(), new SugarPostProcessingExtension(), new WrapMethodApplicatorExtension());
    private final List<Class<? extends InjectionInfo>> ownInjectors = Arrays.asList(ModifyExpressionValueInjectionInfo.class, ModifyReceiverInjectionInfo.class, ModifyReturnValueInjectionInfo.class, WrapOperationInjectionInfo.class, WrapWithConditionV1InjectionInfo.class);
    private final List<Versioned<Class<? extends InjectionInfo>>> ownGatedInjectors = Arrays.asList(new Versioned(MixinExtrasVersion.V0_3_4.getNumber(), WrapWithConditionInjectionInfo.class), new Versioned(MixinExtrasVersion.V0_4_0_BETA_1.getNumber(), WrapMethodInjectionInfo.class));
    private final List<Class<? extends InjectionInfo>> internalInjectors = Arrays.asList(SugarWrapperInjectionInfo.class, FactoryRedirectWrapperInjectionInfo.class);
    private final List<String> registeredInjectors = new ArrayList();
    boolean initialized;

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public int getVersion() {
        return MixinExtrasVersion.LATEST.getNumber();
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public boolean shouldReplace(Object otherService) {
        return getVersion() > MixinExtrasService.getFrom(otherService).getVersion();
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void takeControlFrom(Object olderService) {
        LOGGER.debug("{} is taking over from {}", this, olderService);
        this.ownExtensions.forEach(it -> {
            MixinInternals.registerExtension(it, (it instanceof ServiceInitializationExtension) || (it instanceof MixinTransformerExtension));
        });
        this.ownInjectors.forEach(it2 -> {
            registerInjector(it2, this.ownPackage);
        });
        this.ownGatedInjectors.forEach(it3 -> {
            registerInjector((Class) it3.value, this.ownPackage);
        });
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void concedeTo(Object newerService, boolean wasActive) {
        requireNotInitialized();
        LOGGER.debug("{} is conceding to {}", this, newerService);
        MixinExtrasService newService = MixinExtrasService.getFrom(newerService);
        if (wasActive) {
            deInitialize();
        }
        this.offeredPackages.forEach(packageName -> {
            newService.offerPackage(packageName.version, (String) packageName.value);
        });
        newService.offerPackage(getVersion(), this.ownPackage);
        this.offeredExtensions.forEach(extension -> {
            newService.offerExtension(extension.version, (IExtension) extension.value);
        });
        this.ownExtensions.forEach(extension2 -> {
            newService.offerExtension(getVersion(), extension2);
        });
        this.offeredInjectors.forEach(injector -> {
            newService.offerInjector(injector.version, (Class) injector.value);
        });
        this.ownInjectors.forEach(injector2 -> {
            newService.offerInjector(getVersion(), injector2);
        });
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void offerPackage(int version, String packageName) {
        requireNotInitialized();
        this.offeredPackages.add(new Versioned<>(version, packageName));
        this.allPackages.add(new Versioned<>(version, packageName));
        this.ownInjectors.forEach(it -> {
            registerInjector(it, packageName);
        });
        for (Versioned<Class<? extends InjectionInfo>> gatedInjector : this.ownGatedInjectors) {
            if (version >= gatedInjector.version) {
                registerInjector(gatedInjector.value, packageName);
            }
        }
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void offerExtension(int version, IExtension extension) {
        requireNotInitialized();
        this.offeredExtensions.add(new Versioned<>(version, extension));
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void offerInjector(int version, Class<? extends InjectionInfo> injector) {
        requireNotInitialized();
        this.offeredInjectors.add(new Versioned<>(version, injector));
    }

    public String toString() {
        return String.format("%s(version=%s)", getClass().getName(), MixinExtrasVersion.LATEST);
    }

    @Override // com.llamalad7.mixinextras.service.MixinExtrasService
    public void initialize() {
        requireNotInitialized();
        LOGGER.info("Initializing MixinExtras via {}.", this);
        detectBetaPackages();
        this.internalInjectors.forEach(InjectionInfo::register);
        this.initialized = true;
    }

    private void deInitialize() {
        for (IExtension extension : this.ownExtensions) {
            MixinInternals.unregisterExtension(extension);
        }
        this.registeredInjectors.forEach(MixinInternals::unregisterInjector);
    }

    private void registerInjector(Class<? extends InjectionInfo> injector, String packageName) {
        String name = injector.getAnnotation(InjectionInfo.AnnotationType.class).value().getName();
        String suffix = StringUtils.removeStart(name, this.ownPackage);
        this.registeredInjectors.add(packageName + suffix);
        MixinInternals.registerInjector(packageName + suffix, injector);
    }

    public Type changePackage(Class<?> ourType, Type theirReference, Class<?> ourReference) {
        String suffix = StringUtils.substringAfter(ourReference.getName(), this.ownPackage);
        String theirPackage = StringUtils.substringBefore(theirReference.getClassName(), suffix);
        return Type.getObjectType((theirPackage + StringUtils.substringAfter(ourType.getName(), this.ownPackage)).replace('.', '/'));
    }

    public Set<String> getAllClassNames(String ourName) {
        return getAllClassNamesAtLeast(ourName, Integer.MIN_VALUE);
    }

    public Set<String> getAllClassNamesAtLeast(String ourName, MixinExtrasVersion minVersion) {
        return getAllClassNamesAtLeast(ourName, minVersion.getNumber());
    }

    private Set<String> getAllClassNamesAtLeast(String ourName, int minVersion) {
        String ourBinaryName = ourName.replace('/', '.');
        return (Set) this.allPackages.stream().filter(it -> {
            return it.version >= minVersion;
        }).map(it2 -> {
            return (String) it2.value;
        }).map(it3 -> {
            return StringUtils.replaceOnce(ourBinaryName, this.ownPackage, it3);
        }).collect(Collectors.toSet());
    }

    public boolean isClassOwned(String name) {
        Stream<R> map = this.allPackages.stream().map(it -> {
            return (String) it.value;
        });
        Objects.requireNonNull(name);
        return map.anyMatch(name::startsWith);
    }

    private void requireNotInitialized() {
        if (this.initialized) {
            throw new IllegalStateException("The MixinExtras service has already been selected and is initialized!");
        }
    }

    private void detectBetaPackages() {
        for (IExtension extension : MixinInternals.getExtensions().getActiveExtensions()) {
            String name = extension.getClass().getName();
            if (name.endsWith(".sugar.impl.SugarApplicatorExtension") && !isClassOwned(name)) {
                String packageName = StringUtils.removeEnd(name, ".sugar.impl.SugarApplicatorExtension");
                MixinExtrasVersion version = getBetaVersion(packageName);
                this.allPackages.add(new Versioned<>(version.getNumber(), packageName));
                LOGGER.warn("Found problematic active MixinExtras instance at {} (version {})", packageName, version);
                LOGGER.warn("Versions from 0.2.0-beta.1 to 0.2.0-beta.9 have limited support and it is strongly recommended to update.", new Object[0]);
            }
        }
    }

    private MixinExtrasVersion getBetaVersion(String packageName) {
        String bootstrapClassName = packageName + ".MixinExtrasBootstrap";
        try {
            Class<?> bootstrapClass = Class.forName(bootstrapClassName);
            Field versionField = bootstrapClass.getDeclaredField("VERSION");
            versionField.setAccessible(true);
            String versionName = (String) versionField.get(null);
            switch (versionName) {
                case "0.2.0-beta.1":
                    return MixinExtrasVersion.V0_2_0_BETA_1;
                case "0.2.0-beta.2":
                    return MixinExtrasVersion.V0_2_0_BETA_2;
                case "0.2.0-beta.3":
                    return MixinExtrasVersion.V0_2_0_BETA_3;
                case "0.2.0-beta.4":
                    return MixinExtrasVersion.V0_2_0_BETA_4;
                case "0.2.0-beta.5":
                    return MixinExtrasVersion.V0_2_0_BETA_5;
                case "0.2.0-beta.6":
                    return MixinExtrasVersion.V0_2_0_BETA_6;
                case "0.2.0-beta.7":
                    return MixinExtrasVersion.V0_2_0_BETA_7;
                case "0.2.0-beta.8":
                    return MixinExtrasVersion.V0_2_0_BETA_8;
                case "0.2.0-beta.9":
                    return MixinExtrasVersion.V0_2_0_BETA_9;
                default:
                    throw new IllegalArgumentException("Unrecognized version " + versionName);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to determine version of MixinExtras instance at %s, assuming 0.2.0-beta.1", packageName), e);
            return MixinExtrasVersion.V0_2_0_BETA_1;
        }
    }
}
