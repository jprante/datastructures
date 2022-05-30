package org.xbib.datastructures.trie.patricia;

/**
 * An implementation of {@link KeyAnalyzer} that assumes all keys have the {@link Key} interface implemented.
 */
public class DefaultKeyAnalyzer<K extends Key<K>> extends AbstractKeyAnalyzer<K> {
    @SuppressWarnings("rawtypes")
    private static final DefaultKeyAnalyzer INSTANCE = new DefaultKeyAnalyzer();

    @SuppressWarnings("unchecked")
    public static <K> KeyAnalyzer<K> singleton() {
        return (KeyAnalyzer<K>) INSTANCE;
    }

    @Override
    public int lengthInBits(K key) {
        return key.lengthInBits();
    }

    @Override
    public boolean isBitSet(K key, int bitIndex) {
        return key.isBitSet(bitIndex);
    }

    @Override
    public int bitIndex(K key, K otherKey) {
        return key.bitIndex(otherKey);
    }

    @Override
    public boolean isPrefix(K key, K prefix) {
        return key.isPrefixedBy(prefix);
    }
}
