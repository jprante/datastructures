package org.xbib.datastructures.trie.patricia;

import java.util.Comparator;

/**
 * The {@link KeyAnalyzer} provides bit-level access to keys
 * for the {@link PatriciaTrie}.
 */
public interface KeyAnalyzer<K> extends Comparator<K> {

    /**
     * Returned by {@link #bitIndex(Object, Object)} if a key's
     * bits were all zero (0).
     */
    int NULL_BIT_KEY = -1;

    /**
     * Returned by {@link #bitIndex(Object, Object)} if a the
     * bits of two keys were all equal.
     */
    int EQUAL_BIT_KEY = -2;

    /**
     * Returned by {@link #bitIndex(Object, Object)} if a keys
     * indices are out of bounds.
     */
    int OUT_OF_BOUNDS_BIT_KEY = -3;

    /**
     * Returns the key's length in bits.
     */
    int lengthInBits(K key);

    /**
     * Returns {@code true} if a key's bit it set at the given index.
     */
    boolean isBitSet(K key, int bitIndex);

    /**
     * Returns the index of the first bit that is different in the two keys.
     */
    int bitIndex(K key, K otherKey);

    /**
     * Returns {@code true} if the second argument is a
     * prefix of the first argument.
     */
    boolean isPrefix(K key, K prefix);
}
