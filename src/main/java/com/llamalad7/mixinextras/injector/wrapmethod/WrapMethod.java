package com.llamalad7.mixinextras.injector.wrapmethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/injector/wrapmethod/WrapMethod.class */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapMethod {
    String[] method();

    boolean remap() default true;

    int require() default -1;

    int expect() default 1;

    int allow() default -1;
}
