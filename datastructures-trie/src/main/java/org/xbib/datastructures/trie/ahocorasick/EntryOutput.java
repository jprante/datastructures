package org.xbib.datastructures.trie.ahocorasick;

/**
 * This class is a match, for output.
 *
 * @param <T> Type of the value
 */
public class EntryOutput<T> extends Interval {

    private final String key;

    private final T value;

    public EntryOutput(int start, int end, String key, T value) {
        super(start, end);
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
    public String toString() {
        return super.toString() + "=" + key + (value != null ? "->" + value : "");
    }
}
