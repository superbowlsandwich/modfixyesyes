package com.llamalad7.mixinextras.service;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/service/Versioned.class */
class Versioned<T> {
    final int version;
    final T value;

    Versioned(int version, T value) {
        this.version = version;
        this.value = value;
    }
}
