package com.llamalad7.mixinextras.injector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/LateInjectionApplicatorExtension.class */
public class LateInjectionApplicatorExtension implements IExtension {
    private static final Map<ITargetClassContext, Map<String, List<Runnable[]>>> QUEUED_INJECTIONS = Collections.synchronizedMap(new HashMap());

    static void offerInjection(ITargetClassContext targetClassContext, LateApplyingInjectorInfo injectorInfo) {
        Map<String, List<Runnable[]>> map = QUEUED_INJECTIONS.computeIfAbsent(targetClassContext, k -> {
            return initializeMap();
        });
        List<Runnable[]> list = map.get(injectorInfo.getLateInjectionType());
        Objects.requireNonNull(injectorInfo);
        Objects.requireNonNull(injectorInfo);
        list.add(new Runnable[]{injectorInfo::lateInject, injectorInfo::latePostInject});
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
    }

    public void postApply(ITargetClassContext context) {
        Map<String, List<Runnable[]>> relevant = QUEUED_INJECTIONS.get(context);
        if (relevant == null) {
            return;
        }
        for (List<Runnable[]> queuedInjections : relevant.values()) {
            for (Runnable[] injection : queuedInjections) {
                injection[0].run();
            }
            for (Runnable[] injection2 : queuedInjections) {
                injection2[1].run();
            }
        }
        QUEUED_INJECTIONS.remove(context);
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<String, List<Runnable[]>> initializeMap() {
        Map<String, List<Runnable[]>> result = new LinkedHashMap<>();
        result.put("WrapWithCondition", new ArrayList<>());
        result.put("WrapOperation", new ArrayList<>());
        return result;
    }
}
