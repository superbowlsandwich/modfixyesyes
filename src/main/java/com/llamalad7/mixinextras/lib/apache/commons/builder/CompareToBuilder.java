package com.llamalad7.mixinextras.lib.apache.commons.builder;

import java.util.Comparator;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/lib/apache/commons/builder/CompareToBuilder.class */
public class CompareToBuilder {
    private int comparison = 0;

    public CompareToBuilder append(Object lhs, Object rhs) {
        return append(lhs, rhs, (Comparator<?>) null);
    }

    public CompareToBuilder append(Object lhs, Object rhs, Comparator<?> comparator) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.getClass().isArray()) {
            if (lhs instanceof long[]) {
                append((long[]) lhs, (long[]) rhs);
            } else if (lhs instanceof int[]) {
                append((int[]) lhs, (int[]) rhs);
            } else if (lhs instanceof short[]) {
                append((short[]) lhs, (short[]) rhs);
            } else if (lhs instanceof char[]) {
                append((char[]) lhs, (char[]) rhs);
            } else if (lhs instanceof byte[]) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if (lhs instanceof double[]) {
                append((double[]) lhs, (double[]) rhs);
            } else if (lhs instanceof float[]) {
                append((float[]) lhs, (float[]) rhs);
            } else if (lhs instanceof boolean[]) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                append((Object[]) lhs, (Object[]) rhs, comparator);
            }
        } else if (comparator == null) {
            Comparable<Object> comparable = (Comparable) lhs;
            this.comparison = comparable.compareTo(rhs);
        } else {
            this.comparison = comparator.compare(lhs, rhs);
        }
        return this;
    }

    public CompareToBuilder append(long lhs, long rhs) {
        int i;
        if (this.comparison != 0) {
            return this;
        }
        if (lhs < rhs) {
            i = -1;
        } else {
            i = lhs > rhs ? 1 : 0;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(int lhs, int rhs) {
        int i;
        if (this.comparison != 0) {
            return this;
        }
        if (lhs < rhs) {
            i = -1;
        } else {
            i = lhs > rhs ? 1 : 0;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(short lhs, short rhs) {
        int i;
        if (this.comparison != 0) {
            return this;
        }
        if (lhs < rhs) {
            i = -1;
        } else {
            i = lhs > rhs ? 1 : 0;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(char lhs, char rhs) {
        int i;
        if (this.comparison != 0) {
            return this;
        }
        if (lhs < rhs) {
            i = -1;
        } else {
            i = lhs > rhs ? 1 : 0;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(byte lhs, byte rhs) {
        int i;
        if (this.comparison != 0) {
            return this;
        }
        if (lhs < rhs) {
            i = -1;
        } else {
            i = lhs > rhs ? 1 : 0;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(double lhs, double rhs) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = Double.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(float lhs, float rhs) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = Float.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(boolean lhs, boolean rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (!lhs) {
            this.comparison = -1;
        } else {
            this.comparison = 1;
        }
        return this;
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator<?> comparator) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }

    public CompareToBuilder append(long[] lhs, long[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(int[] lhs, int[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(short[] lhs, short[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(char[] lhs, char[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(byte[] lhs, byte[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(double[] lhs, double[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(float[] lhs, float[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
        if (this.comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            this.comparison = -1;
            return this;
        }
        if (rhs == null) {
            this.comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public int toComparison() {
        return this.comparison;
    }
}
