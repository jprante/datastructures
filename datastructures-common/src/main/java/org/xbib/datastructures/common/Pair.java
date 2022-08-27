package org.xbib.datastructures.common;

import java.util.Objects;

/**
 * A pair of data, allowing null for key and value, in difference to Map.entry().
 *
 * @param <K> the key type parameter
 * @param <V> the value type parameter
 */
public class Pair<K, V> implements Comparable<Pair<K, V>> {

    private final K key;

    private final V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int compareTo(Pair<K, V> pair) {
        return 0;
    }
}
