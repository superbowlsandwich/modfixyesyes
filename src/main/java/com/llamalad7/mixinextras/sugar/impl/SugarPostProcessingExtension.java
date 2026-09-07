package com.llamalad7.mixinextras.sugar.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/SugarPostProcessingExtension.class */
public class SugarPostProcessingExtension implements IExtension {
    private static final Map<String, List<Task>> POST_PROCESSING_TASKS = new HashMap();

    static void enqueuePostProcessing(SugarApplicator applicator, Runnable task) {
        POST_PROCESSING_TASKS.computeIfAbsent(applicator.info.getClassNode().name, k -> {
            return new ArrayList();
        }).add(new Task(applicator.postProcessingPriority(), task));
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
    }

    public void postApply(ITargetClassContext context) {
        String targetName = context.getClassNode().name;
        List<Task> tasks = POST_PROCESSING_TASKS.remove(targetName);
        if (tasks != null) {
            Collections.sort(tasks);
            tasks.forEach((v0) -> {
                v0.run();
            });
        }
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/SugarPostProcessingExtension$Task.class */
    private static class Task implements Comparable<Task> {
        private final int priority;
        private final Runnable body;

        public Task(int priority, Runnable body) {
            this.priority = priority;
            this.body = body;
        }

        public void run() {
            this.body.run();
        }

        @Override // java.lang.Comparable
        public int compareTo(Task o) {
            return Integer.compare(this.priority, o.priority);
        }
    }
}
