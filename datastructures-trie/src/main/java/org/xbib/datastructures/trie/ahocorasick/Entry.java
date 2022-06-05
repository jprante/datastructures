package org.xbib.datastructures.trie.ahocorasick;

/**
 *  An entry, a key with a value
 *
 * @param <T> The type of the value.
 */
public class Entry<T> implements Comparable<Entry<T>> {

    private final String key;

    private final T value;

    public Entry(String key, T value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry<T> other) {
        return key.compareTo(other.getKey());
    }
}
