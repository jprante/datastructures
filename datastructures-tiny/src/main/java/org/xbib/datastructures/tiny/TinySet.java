package org.xbib.datastructures.tiny;

import java.util.Arrays;
import java.util.Objects;

public abstract class TinySet<T> extends IndexedSetBase<T> {

    public static int tableSize(int length) {
        return Integer.highestOneBit(length * 2 - 1) * 2;
    }

    private static int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode() * 0x85ebca6b) ^ h >>> 16;
    }

    public static <T> TinySet<T> createUnsafe(Object[] keys) {
        if (keys.length == 0) {
            return new Empty<>();
        } else if (keys.length < 0xFF) {
            return new Small<>(keys);
        } else if (keys.length < 0xFFFF) {
            return new Medium<>(keys);
        } else {
            return new Large<>(keys);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Empty<T> extends TinySet<T> implements Immutable<T> {

        @Override
        public int getIndex(Object key) {
            return -1;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public T getEntryAt(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    private static abstract class ArrayTableSet<T, A> extends TinySet<T> implements Immutable<T> {

        protected final Object[] keys;

        protected final A table;

        private ArrayTableSet(Object[] keys) {
            this.keys = keys;
            this.table = newTable(tableSize(keys.length));
            for (int j = 0; j < keys.length; j++) {
                Object key = keys[j];
                int hash = ~getIndex(key);
                Preconditions.checkArgument(hash >= 0, "duplicate key: %s", key);
                tableSet(table, hash, j);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getEntryAt(int index) {
            return (T) keys[index];
        }

        @Override
        public int size() {
            return keys.length;
        }

        protected abstract A newTable(int size);

        protected abstract void tableSet(A table, int index, int value);
    }

    public static class Small<T> extends ArrayTableSet<T, byte[]> {

        private Small(Object[] keys) {
            super(keys);
        }

        @Override
        protected byte[] newTable(int size) {
            byte[] table = new byte[size];
            Arrays.fill(table, (byte) 0xFF);
            return table;
        }

        @Override
        protected void tableSet(byte[] table, int index, int value) {
            table[index] = (byte) value;
        }

        @Override
        public int getIndex(Object key) {
            byte[] table = this.table;
            int mask = table.length - 1;
            int hash = hash(key) & mask;
            int collisions = 0;
            for (int i = table[hash] & 0xFF; i < 0xFF; i = table[hash = (hash + ++collisions) & mask] & 0xFF) {
                if (Objects.equals(key, keys[i])) {
                    return i;
                }
            }
            return ~hash;
        }
    }

    public static class Medium<T> extends ArrayTableSet<T, short[]> {

        private Medium(Object[] keys) {
            super(keys);
        }

        @Override
        protected short[] newTable(int size) {
            short[] table = new short[size];
            Arrays.fill(table, (short) 0xFFFF);
            return table;
        }

        @Override
        protected void tableSet(short[] table, int index, int value) {
            table[index] = (short) value;
        }

        @Override
        public int getIndex(Object key) {
            short[] table = this.table;
            int mask = table.length - 1;
            int hash = hash(key) & mask;
            int collisions = 0;
            for (int i = table[hash] & 0xFFFF; i < 0xFFFF; i = table[hash = (hash + ++collisions) & mask] & 0xFFFF) {
                if (Objects.equals(key, keys[i])) {
                    return i;
                }
            }
            return ~hash;
        }
    }

    public static class Large<T> extends ArrayTableSet<T, int[]> {

        private Large(Object[] keys) {
            super(keys);
        }

        @Override
        protected int[] newTable(int size) {
            int[] table = new int[size];
            Arrays.fill(table, -1);
            return table;
        }

        @Override
        protected void tableSet(int[] table, int index, int value) {
            table[index] = value;
        }

        @Override
        public int getIndex(Object key) {
            int[] table = this.table;
            int mask = table.length - 1;
            int hash = hash(key) & mask;
            int collisions = 0;
            for (int i = table[hash]; i >= 0; i = table[hash = (hash + ++collisions) & mask]) {
                if (Objects.equals(key, keys[i])) {
                    return i;
                }
            }
            return ~hash;
        }
    }

    public static class Builder<T> extends IndexedSetBase<T> implements IndexedCollectionBase.NoAdditiveChange<T> {

        private static final Object TOMBSTONE = new Object() {

            @Override
            public String toString() {
                return "TOMBSTONE";
            }
        };

        private Object[] keys;

        private int[] inverse;

        private int[] table;

        private int rawSize = 0;

        private int size = 0;

        Builder() {
            this(16);
        }

        Builder(int expectedSize) {
            this.keys = new Object[expectedSize];
            this.inverse = new int[expectedSize];
            forceRehash(TinySet.tableSize(expectedSize));
        }

        private static int hash(Object key) {
            int h;
            return key == null ? 0 : (h = key.hashCode() * 0x85ebca6b) ^ h >>> 16;
        }

        private static int[] newTable(int size) {
            int[] table = new int[size];
            Arrays.fill(table, -1);
            return table;
        }

        public void compact() {
            if (rawSize == size) {
                return;
            }
            softClearTable();
            int index = 0;
            for (int i = 0; i < rawSize; i++) {
                if (keys[i] == TOMBSTONE) {
                    continue;
                }
                keys[index] = keys[i];
                int hash = ~getIndex(keys[index]);
                table[hash] = index;
                inverse[index] = hash;
                index++;
            }
            Arrays.fill(keys, index, rawSize, null);
            this.size = index;
            this.rawSize = index;
        }

        private void forceRehash(int newSize) {
            this.table = newTable(newSize);
            this.size = 0;
            compact();
        }

        private void softClearTable() {
            for (int i = 0; i < rawSize; i++) {
                table[inverse[i]] = -1;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getEntryAt(int index) {
            Preconditions.checkElementIndex(index, rawSize);
            return (T) keys[index];
        }

        @Override
        public int addOrGetIndex(T key) {
            int index = getIndex(key);
            if (index >= 0) {
                return index;
            }
            index = checkOverflow(key, index);
            int hash = ~index;
            int newIndex = rawSize++;
            keys[newIndex] = key;
            table[hash] = newIndex;
            inverse[newIndex] = hash;
            size++;
            return ~newIndex;
        }

        private int checkOverflow(T key, int index) {
            if (rawSize == keys.length) {
                int newSize = keys.length + (keys.length >> 1);
                keys = Arrays.copyOf(keys, newSize);
                inverse = Arrays.copyOf(inverse, newSize);
            }
            if (2 * (rawSize + 1) > table.length) {
                forceRehash(table.length * 2);
                index = getIndex(key);
            }
            return index;
        }

        @Override
        public int getIndex(Object key) {
            int collisions = 0;
            int mask = table.length - 1;
            int hash = hash(key) & mask;
            for (int i = table[hash]; i >= 0; i = table[hash = (hash + ++collisions) & mask]) {
                if (Objects.equals(key, keys[i])) {
                    return i;
                }
            }
            return ~hash;
        }

        @Override
        public boolean removeAt(int index) {
            Preconditions.checkElementIndex(index, rawSize);
            keys[index] = TOMBSTONE;
            size--;
            return false;
        }

        @Override
        public boolean isRemoved(int index) {
            return keys[index] == TOMBSTONE;
        }

        public int size() {
            return size;
        }

        @Override
        public int rawSize() {
            return rawSize;
        }

        public TinySet<T> build() {
            compact();
            return TinySet.createUnsafe(Arrays.copyOf(keys, size));
        }

        @Override
        public void clear() {
            Arrays.fill(keys, 0, rawSize, null);
            softClearTable();
            size = rawSize = 0;
        }
    }
}
