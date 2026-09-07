package com.llamalad7.mixinextras.utils;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/UniquenessHelper.class */
public class UniquenessHelper {
    public static String getUniqueMethodName(ClassNode classNode, String name) {
        int counter = classNode.methods.size();
        while (true) {
            String candidate = name + '$' + counter;
            boolean isValid = true;
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(candidate)) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) {
                counter++;
            } else {
                return candidate;
            }
        }
    }
}
