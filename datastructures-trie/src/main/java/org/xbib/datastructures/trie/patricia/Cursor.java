package org.xbib.datastructures.trie.patricia;

import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link Cursor} can be used to traverse a {@link PrefixTree}, visit each node
 * step by step and make {@link Decision}s on each step how to continue with
 * traversing the {@link PrefixTree}.
 */
public interface Cursor<K, V> {

    /**
     * Called for each {@link Entry} in the {@link PrefixTree}. Return
     * {@link Decision#EXIT} to finish the {@link PrefixTree} operation,
     * {@link Decision#CONTINUE} to go to the next {@link Entry},
     * {@link Decision#REMOVE} to remove the {@link Entry} and
     * continue iterating or {@link Decision#REMOVE_AND_EXIT} to
     * remove the {@link Entry} and stop iterating.
     * <p>
     * Note: Not all operations support {@link Decision#REMOVE}.
     */
    Decision select(Map.Entry<? extends K, ? extends V> entry);

    /**
     * The {@link Decision} tells the {@link Cursor} what to do on each step
     * while traversing the {@link PrefixTree}.
     * <p>
     * NOTE: Not all operations that work with a {@link Cursor} support all
     * {@link Decision} types
     */
    enum Decision {

        /**
         * Exit the traverse operation
         */
        EXIT,

        /**
         * Continue with the traverse operation
         */
        CONTINUE,

        /**
         * Remove the previously returned element
         * from the {@link PrefixTree} and continue
         */
        REMOVE,

        /**
         * Remove the previously returned element
         * from the {@link PrefixTree} and exit from the
         * traverse operation
         */
        REMOVE_AND_EXIT
    }
}