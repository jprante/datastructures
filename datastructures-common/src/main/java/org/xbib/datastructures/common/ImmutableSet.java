package org.xbib.datastructures.common;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A light-weight immutable set.
 */
public final class ImmutableSet<T> extends AbstractSet<T> {

    /*
     * The internal array. The constructors copy values into this array and the array is never changed.
     */
    private final T[] array;

    public ImmutableSet(Set<T> set, T[] t) {
        this(set.toArray(t), false);
    }

    public ImmutableSet(Set<T> set, T[] t, boolean filterNull) {
        this(set.toArray(t), filterNull);
    }

    public ImmutableSet(T[] array) {
        this(array, false);
    }

    public ImmutableSet(T[] array, boolean filterNulls) {
        this.array = filterNulls ? arrayFilteringNull(array) : array;
    }

    public static <T> ImmutableSet<T> of() {
        return new ImmutableSet<>(null);
    }

    public static <T> ImmutableSet<T> of(Set<T> set, T[] t) {
        return new ImmutableSet<>(set, t);
    }

    public static <T> ImmutableSet<T> of(Set<T> set, T[] t, boolean filterNull) {
        return new ImmutableSet<>(set, t, filterNull);
    }

    public static <T> ImmutableSet<T> of(T[] array) {
        return new ImmutableSet<>(array);
    }

    public static <T> ImmutableSet<T> of(T[] array, boolean filterNull) {
        return new ImmutableSet<>(array, filterNull);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    public Iterator<T> iterator() {
        return array != null ? Arrays.stream(array).sorted().distinct().iterator() : Collections.emptyIterator();
    }

    @Override
    public int size() {
        return array != null? array.length : 0;
    }

    @Override
    public boolean contains(Object o) {
        if (array != null) {
            for (T t : array) {
                if (t == o) {
                    return true;
                }
            }
        }
        return false;
    }

    private static <T> T[] arrayFilteringNull(T[] views) {
        Objects.requireNonNull(views);
        int end = 0;
        int length = views.length;
        for (int i = 0; i < length; i++) {
            T view = views[i];
            if (view != null) {
                views[end++] = view;
            }
        }
        return end == length ? views : Arrays.copyOf(views, end);
    }

    public static class Builder<T> {

        private final Set<T> set = new LinkedHashSet<>();

        private Builder() {
        }

        public Builder<T> add(T t) {
            set.add(t);
            return this;
        }

        public ImmutableSet<T> build(T[] t) {
            return new ImmutableSet<>(set, t);
        }
    }
}
