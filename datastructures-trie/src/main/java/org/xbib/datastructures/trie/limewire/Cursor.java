package org.xbib.datastructures.trie.limewire;

import java.util.Map;

/**
 * An interface used by a {@link Trie}. A {@link Trie} selects items by
 * closeness and passes the items to the <code>Cursor</code>. You can then
 * decide what to do with the key-value pair and the return value
 * from {@link #select(java.util.Map.Entry)} tells the <code>Trie</code>
 * what to do next.
 * <p>
 * <code>Cursor</code> returns status/selection status might be:
 * <table cellspace="5">
 * <tr><td><b>Return Value</b></td><td><b>Status</b></td></tr>
 * <tr><td>EXIT</td><td>Finish the Trie operation</td></tr>
 * <tr><td>CONTINUE</td><td>Look at the next element in the traversal</td></tr>
 * <tr><td>REMOVE_AND_EXIT</td><td>Remove the entry and stop iterating</td></tr>
 * <tr><td>REMOVE</td><td>Remove the entry and continue iterating</td></tr>
 * </table>
 * <p>
 * Note: {@link Trie#select(Object, Cursor)} does
 * not support <code>REMOVE</code>.
 *
 * @param <K> Key Type
 * @param <V> Key Value
 */
public interface Cursor<K, V> {

    /**
     * Notification that the Trie is currently looking at the given entry.
     * Return <code>EXIT</code> to finish the Trie operation,
     * <code>CONTINUE</code> to look at the next entry, <code>REMOVE</code>
     * to remove the entry and continue iterating, or
     * <code>REMOVE_AND_EXIT</code> to remove the entry and stop iterating.
     * Not all operations support <code>REMOVE</code>.
     */
    SelectStatus select(Map.Entry<? extends K, ? extends V> entry);

    /**
     * The mode during selection.
     */
    enum SelectStatus {
        EXIT,
        CONTINUE,
        REMOVE,
        REMOVE_AND_EXIT
    }
}
