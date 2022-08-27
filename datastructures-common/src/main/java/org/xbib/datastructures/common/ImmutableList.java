package org.xbib.datastructures.common;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * A light-weight immutable list.
 */
public final class ImmutableList<T> extends AbstractList<T> implements RandomAccess {

    /*
     * The internal array. The constructors copy values into this array and the array is never changed.
     */
    private final T[] array;

    public ImmutableList(List<T> list, T[] t) {
        this(list.toArray(t), false);
    }

    public ImmutableList(List<T> list, T[] t, boolean filterNull) {
        this(list.toArray(t), filterNull);
    }

    public ImmutableList(T[] array) {
        this(array, false);
    }

    public ImmutableList(T[] array, boolean filterNulls) {
        this.array = filterNulls ? arrayFilteringNull(array) : array;
    }

    public static <T> ImmutableList<T> of() {
        return new ImmutableList<>(null);
    }

    public static <T> ImmutableList<T> of(List<T> list, T[] t) {
        return new ImmutableList<>(list, t);
    }

    public static <T> ImmutableList<T> of(List<T> list, T[] t, boolean filterNull) {
        return new ImmutableList<>(list, t, filterNull);
    }

    public static <T> ImmutableList<T> of(T[] array) {
        return new ImmutableList<>(array);
    }

    public static <T> ImmutableList<T> of(T[] array, boolean filterNull) {
        return new ImmutableList<>(array, filterNull);
    }

    @Override
    public T get(int index) {
        return array != null ? array[index] : null;
    }

    @Override
    public int size() {
        return array != null ? array.length : 0;
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
}
