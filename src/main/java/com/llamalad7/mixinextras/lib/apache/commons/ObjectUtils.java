package com.llamalad7.mixinextras.lib.apache.commons;

import java.io.Serializable;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/lib/apache/commons/ObjectUtils.class */
public class ObjectUtils {
    public static final Null NULL = new Null();

    @Deprecated
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    public String toString() {
        return super.toString();
    }

    /* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/lib/apache/commons/ObjectUtils$Null.class */
    public static class Null implements Serializable {
        Null() {
        }
    }
}
