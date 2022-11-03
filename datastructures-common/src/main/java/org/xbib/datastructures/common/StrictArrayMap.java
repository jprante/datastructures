package org.xbib.datastructures.common;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class StrictArrayMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private final K[] keys;

    private final V[] values;

    public StrictArrayMap(K[] keys, V[] values) {
        this.keys = keys;
        this.values = values;
    }

    public K[] theKeys() {
        return keys;
    }

    public V[] theValues() {
        return values;
    }

    @SuppressWarnings("unchecked")
    public StrictArrayMap(Collection<K> keys, Collection<V> values) {
        this.keys = (K[]) keys.toArray();
        this.values = (V[]) values.toArray();
    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    public boolean isEmpty() {
        return keys.length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Object o : keys) {
            if (o == null) {
                if (key == null) {
                    return true;
                }
            } else {
                if (o.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object o : values) {
            if (o == null) {
                if (value == null) {
                    return true;
                }
            } else {
                if (o.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < keys.length; i++ ) {
            if (keys[i].equals(key)) {
                return values[i];
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        return new StrictArraySet<>(keys);
    }

    @Override
    public Collection<V> values() {
        return new StrictArraySet<>(values);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Set<Entry<K, V>> entrySet() {
        Map.Entry<K, V>[] entries = new Map.Entry[keys.length];
        for (int i = 0; i < keys.length; i++) {
            entries[i] = new EntryHolder<>(keys[i], values[i]);
        }
        return new StrictArraySet<>(entries);
    }

    static class EntryHolder<K,V> implements Map.Entry<K,V> {

        final K key;

        final V value;

        EntryHolder(K k, V v) {
            key = k;
            value = v;
        }

        /**
         * Gets the key from this holder.
         *
         * @return the key
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Gets the value from this holder.
         *
         * @return the value
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Throws {@link UnsupportedOperationException}.
         *
         * @param value ignored
         * @return never returns normally
         */
        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("not supported");
        }

        /**
         * Compares the specified object with this entry for equality.
         * Returns {@code true} if the given object is also a map entry and
         * the two entries' keys and values are equal. Note that key and
         * value are non-null, so equals() can be called safely on them.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            return key != null && key.equals(e.getKey()) && value != null &&  value.equals(e.getValue());
        }

        /**
         * Returns the hash code value for this map entry. The hash code
         * is {@code key.hashCode() ^ value.hashCode()}. Note that key and
         * value are non-null, so hashCode() can be called safely on them.
         */
        @Override
        public int hashCode() {
            return key != null && value != null ? key.hashCode() ^ value.hashCode() : 0;
        }

        /**
         * Returns a String representation of this map entry.  This
         * implementation returns the string representation of this
         * entry's key followed by the equals character ("{@code =}")
         * followed by the string representation of this entry's value.
         *
         * @return a String representation of this map entry
         */
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
