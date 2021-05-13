package org.xbib.datastructures.tiny;

import java.util.Arrays;

public class TinyList<T> extends IndexedListBase<T> implements IndexedListBase.Immutable<T> {

    private final Object[] elements;

    private TinyList(Object[] elements) {
        this.elements = elements;
    }

    public static <T> TinyList.Builder<T> builder() {
        return new TinyList.Builder<>();
    }

    @Override
    public int size() {
        return this.elements.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getEntryAt(int index) {
        return (T) elements[index];
    }

    public static class Builder<T>
            extends IndexedListBase<T>
            implements IndexedCollectionBase.NoAdditiveChange<T> {

        private Object[] values;

        private int size;

        private Builder() {
            this(2);
        }

        private Builder(int initialSize) {
            values = new Object[initialSize];
            clear();
        }

        @Override
        public int addOrGetIndex(T obj) {
            int index = size++;
            if (index == values.length) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[index] = obj;
            return ~index;
        }

        @Override
        public T set(int index, T obj) {
            T old = getEntryAt(index);
            values[index] = obj;
            return old;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getEntryAt(int index) {
            Preconditions.checkElementIndex(index, size);
            return (T) values[index];
        }

        @Override
        public void clear() {
            Arrays.fill(values, 0, size, null);
            size = 0;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public T removeLast() {
            T old = set(size - 1, null);
            size--;
            return old;
        }

        public TinyList<T> build() {
            return new TinyList<>(Arrays.copyOf(values, size));
        }
    }
}
