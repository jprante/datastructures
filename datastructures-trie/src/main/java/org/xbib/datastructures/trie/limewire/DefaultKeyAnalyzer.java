package org.xbib.datastructures.trie.limewire;

/**
 * An implementation of {@link KeyAnalyzer}
 * that assumes all keys have the {@link Key} interface implemented.
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
    public boolean isBitSet(K key, int keyLength, int bitIndex) {
        return key.isBitSet(bitIndex);
    }

    @Override
    public int bitIndex(K key, int keyStart, int keyLength, K found, int foundStart, int foundLength) {
        return key.bitIndex(found);
    }

    @Override
    public int bitsPerElement() {
        return 16;
    }

    @Override
    public boolean isPrefix(K prefix, int offset, int length, K key) {
        return key.isPrefixedBy(prefix);
    }
}
