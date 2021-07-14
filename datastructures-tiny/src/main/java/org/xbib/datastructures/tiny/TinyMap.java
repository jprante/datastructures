package org.xbib.datastructures.tiny;

import java.util.Arrays;

public abstract class TinyMap<K, V> extends IndexedMapBase<K, V>  {

    private final TinySet<K> keys;

    protected TinyMap(TinySet<K> keys) {
        this.keys = keys;
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    @Override
    public K getKeyAt(int index) {
        return keys.getEntryAt(index);
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public int getIndex(Object key) {
        return keys.getIndex(key);
    }

    @Override
    public TinySet<K> keySet() {
        return keys;
    }

    public static final Object TOMBSTONE = new Object() {
        @Override
        public String toString() {
            return "TOMBSTONE";
        }
    };

    public static class Builder<K, V>
            extends IndexedMapBase<K, V>
            implements Comparable<Builder<K, V>> {

        private final TinySet.Builder<K> keys;

        private Object[] values;

        public Builder() {
            this(16);
        }

        private Builder(int expectedSize) {
            values = new Object[expectedSize];
            keys = new TinySet.Builder<>(expectedSize) {

                @Override
                public void compact() {
                    if (size() == rawSize()) {
                        return;
                    }
                    int index = 0;
                    int rawSize = rawSize();
                    for (int i = 0; i < rawSize; i++) {
                        if (values[i] == TOMBSTONE) {
                            continue;
                        }
                        values[index++] = values[i];
                    }
                    Arrays.fill(values, index, rawSize, null);
                    super.compact();
                }
            };
        }

        @Override
        public V put(K key, V value) {
            int index = keys.addOrGetIndex(key);
            if (index >= 0) {
                return setValueAt(index, value);
            }
            index = ~index;
            if (index >= values.length) {
                values = Arrays.copyOf(values, values.length + (values.length >> 1));
            }
            values[index] = value;
            return null;
        }

        @Override
        public int getIndex(Object key) {
            return keys.getIndex(key);
        }

        @Override
        public K getKeyAt(int index) {
            return keys.getEntryAt(index);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V getValueAt(int index) {
            Preconditions.checkElementIndex(index, rawSize());
            return (V) values[index];
        }

        @SuppressWarnings("unchecked")
        @Override
        public V setValueAt(int index, V value) {
            Preconditions.checkElementIndex(index, rawSize());
            Object old = values[index];
            values[index] = value;
            return (V) old;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V removeAt(int index) {
            keys.removeAt(index);
            Object old = values[index];
            values[index] = TOMBSTONE;
            return (V) old;
        }

        @Override
        public boolean isRemoved(int index) {
            return keys.isRemoved(index);
        }

        public int size() {
            return keys.size();
        }

        @Override
        public int rawSize() {
            return keys.rawSize();
        }

        @Override
        public void clear() {
            Arrays.fill(values, 0, keys.rawSize(), null);
            keys.clear();
        }

        @Override
        public int hashCode() {
            return keys.hashCode() ^ Arrays.hashCode(values);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Builder)) {
                return false;
            }
            Builder<K, V> other = (Builder<K, V>) obj;
            if (!keySet().equals(other.keySet())) {
                return false;
            }
            return Arrays.equals(values, other.values);
        }

        @Override
        public int compareTo(Builder<K, V> o) {
            return keySet().compareTo(o.keySet());
        }

        public TinyMap<K, V> buildWithKeys(TinySet<K> keys) {
            compact();
            Preconditions.checkArgument(keys.size() == size(),
                    "Must have same size");
            return new SizeAny<>(keys, Arrays.copyOf(values, keys.size()));
        }

        public void compact() {
            keys.compact();
        }

        public TinyMap<K, V> build() {
            return buildWithKeys(this.keys.build());
        }
    }

    public static class SizeAny<K, V> extends TinyMap<K, V> {

        private final Object[] values;

        public SizeAny(TinySet<K> keys, Object[] values) {
            super(keys);
            Preconditions.checkArgument(keys.size() == values.length,
                    "keys and values must have same size");
            this.values = values;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V getValueAt(int index) {
            return (V) values[index];
        }
    }
}
