package com.llamalad7.mixinextras.lib.apache.commons.tuple;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/lib/apache/commons/tuple/ImmutablePair.class */
public final class ImmutablePair<L, R> extends Pair<L, R> {
    public final L left;
    public final R right;

    public ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override // com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair
    public L getLeft() {
        return this.left;
    }

    @Override // com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair
    public R getRight() {
        return this.right;
    }

    @Override // java.util.Map.Entry
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }
}
