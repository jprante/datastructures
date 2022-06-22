package org.xbib.datastructures.trie.limewire;

import java.util.Comparator;

/**
 * Defines the interface to analyze {@link Trie} keys on a bit
 * level. <code>KeyAnalyzer</code>'s
 * methods return the length of the key in bits, whether or not a bit is
 * set, and bits per element in the key.
 * <p>
 * Additionally, a method determines if a key is a prefix of another key and
 * returns the bit index where one key is different from another key (if
 * the key and found key are equal than the return value is EQUAL_BIT_KEY).
 * <p>
 * <code>KeyAnalyzer</code> defines:<br>
 * <table cellspace="5">
 * <tr><td>NULL_BIT_KEY</td><td>When key's bits are all zero</td></tr>
 * <tr><td> EQUAL_BIT_KEY </td><td>When keys are the same </td></tr>
 * </table>
 */
public interface KeyAnalyzer<K> extends Comparator<K> {

    /**
     * Returned by bitIndex if key's bits are all 0.
     */
    int NULL_BIT_KEY = -1;

    /**
     * Returned by bitIndex if key and found key are
     * equal. This is a very very specific case and
     * shouldn't happen on a regular basis.
     */
    int EQUAL_BIT_KEY = -2;

    /**
     * Returns the length of the Key in bits.
     */
    int lengthInBits(K key);

    /**
     * Returns whether or not a bit is set.
     */
    boolean isBitSet(K key, int keyLength, int bitIndex);

    /**
     * Returns the n-th different bit between key and found.
     * This starts the comparison in key at 'keyStart' and goes
     * for 'keyLength' bits, and compares to the found key
     * starting at 'foundStart' and going for 'foundLength' bits.
     */
    int bitIndex(K key, int keyStart, int keyLength, K found, int foundStart, int foundLength);

    /**
     * Returns the number of bits per element in the key.
     * This is only useful for variable-length keys, such as Strings.
     */
    int bitsPerElement();

    /**
     * Determines whether or not the given prefix (from offset to length)
     * is a prefix of the given key.
     */
    boolean isPrefix(K prefix, int offset, int length, K key);
}
