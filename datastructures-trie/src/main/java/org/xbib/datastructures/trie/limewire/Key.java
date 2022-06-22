package org.xbib.datastructures.trie.limewire;

/**
 * An interface that {@link PatriciaTrie} keys may implement.
 *
 * @see KeyAnalyzer
 * @see DefaultKeyAnalyzer
 */
public interface Key<K> {

    /**
     * Returns the key's length in bits.
     */
    int lengthInBits();

    /**
     * Returns {@code true} if the given bit is set.
     */
    boolean isBitSet(int bitIndex);

    /**
     * Returns the index of the first bit that is different in the two keys.
     */
    int bitIndex(K otherKey);

    /**
     * Returns {@code true} if this key is prefixed by the given key.
     */
    boolean isPrefixedBy(K prefix);
}
