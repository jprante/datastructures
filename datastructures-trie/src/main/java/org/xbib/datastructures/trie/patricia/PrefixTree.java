package org.xbib.datastructures.trie.patricia;

import java.util.Map;
import java.util.SortedMap;

/**
 * Defines the interface for a prefix tree, an ordered tree data structure. For
 * more information, see <a href="http://en.wikipedia.org/wiki/Trie">Tries</a>.
 */
public interface PrefixTree<K, V> extends SortedMap<K, V> {

    /**
     * Returns the {@link Map.Entry} whose key is closest in a bitwise XOR
     * metric to the given key. This is NOT lexicographic closeness.
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     * <p>
     * If the {@link PrefixTree} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @return The {@link Map.Entry} whose key is closest in a bitwise XOR metric
     * to the provided key.
     */
    Map.Entry<K, V> select(K key);

    /**
     * Returns the key that is closest in a bitwise XOR metric to the
     * provided key. This is NOT lexicographic closeness!
     * <p>
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     * <p>
     * If the {@link PrefixTree} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @return The key that is closest in a bitwise XOR metric to the provided key.
     */
    K selectKey(K key);

    /**
     * Returns the value whose key is closest in a bitwise XOR metric to
     * the provided key. This is NOT lexicographic closeness!
     * <p>
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     * <p>
     * If the {@link PrefixTree} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @return The value whose key is closest in a bitwise XOR metric
     * to the provided key.
     */
    V selectValue(K key);

    /**
     * Iterates through the {@link PrefixTree}, starting with the entry whose bitwise
     * value is closest in an XOR metric to the given key. After the closest
     * entry is found, the {@link PrefixTree} will call select on that entry and continue
     * calling select for each entry (traversing in order of XOR closeness,
     * NOT lexicographically) until the cursor returns {@link Cursor.Decision#EXIT}.
     *
     * <p>The cursor can return {@link Cursor.Decision#CONTINUE} to continue traversing.
     *
     * <p>{@link Cursor.Decision#REMOVE_AND_EXIT} is used to remove the current element
     * and stop traversing.
     *
     * <p>Note: The {@link Cursor.Decision#REMOVE} operation is not supported.
     *
     * @return The entry the cursor returned {@link Cursor.Decision#EXIT} on, or null
     * if it continued till the end.
     */
    Map.Entry<K, V> select(K key, Cursor<? super K, ? super V> cursor);

    /**
     * Traverses the {@link PrefixTree} in lexicographical order.
     * {@link Cursor#select(java.util.Map.Entry)} will be called on each entry.
     *
     * <p>The traversal will stop when the cursor returns {@link Cursor.Decision#EXIT},
     * {@link Cursor.Decision#CONTINUE} is used to continue traversing and
     * {@link Cursor.Decision#REMOVE} is used to remove the element that was selected
     * and continue traversing.
     *
     * <p>{@link Cursor.Decision#REMOVE_AND_EXIT} is used to remove the current element
     * and stop traversing.
     *
     * @return The entry the cursor returned {@link Cursor.Decision#EXIT} on, or null
     * if it continued till the end.
     */
    Map.Entry<K, V> traverse(Cursor<? super K, ? super V> cursor);

    /**
     * Returns a view of this {@link PrefixTree} of all elements that are prefixed
     * by the given key.
     *
     * <p>In a {@link PrefixTree} with fixed size keys, this is essentially a
     * {@link #get(Object)} operation.
     *
     * <p>For example, if the {@link PrefixTree} contains 'Anna', 'Anael',
     * 'Analu', 'Andreas', 'Andrea', 'Andres', and 'Anatole', then
     * a lookup of 'And' would return 'Andreas', 'Andrea', and 'Andres'.
     */
    SortedMap<K, V> prefixMap(K prefix);
}
