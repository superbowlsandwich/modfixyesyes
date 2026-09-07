package com.llamalad7.mixinextras.sugar.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/SingleIterationList.class */
public class SingleIterationList<T> implements List<T> {
    private final List<T> delegate;
    private final int allowedIteration;
    private int iteration;

    public SingleIterationList(List<T> delegate, int allowedIteration) {
        this.delegate = delegate;
        this.allowedIteration = allowedIteration;
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        int i = this.iteration;
        this.iteration = i + 1;
        if (i == this.allowedIteration) {
            return this.delegate.iterator();
        }
        return Collections.emptyIterator();
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
