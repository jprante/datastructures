package org.xbib.datastructures.trie.concurrent.util;

/**
 * A super-interface of both {@link Node} and {@link NodeCharacterKey}
 * which, by sharing this common interface, enables binary search of nodes via
 * {@link java.util.Collections#binarySearch(java.util.List, Object, java.util.Comparator)}.
 *
 * @see NodeCharacterComparator
 * @see NodeCharacterKey
 */
public interface NodeCharacterProvider {

    Character getIncomingEdgeFirstCharacter();
}
