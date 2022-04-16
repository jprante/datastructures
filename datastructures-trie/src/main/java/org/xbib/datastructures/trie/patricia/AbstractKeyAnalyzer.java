package org.xbib.datastructures.trie.patricia;

/**
 * An abstract implementation of {@link KeyAnalyzer}.
 */
public abstract class AbstractKeyAnalyzer<K> implements KeyAnalyzer<K> {

    @SuppressWarnings("unchecked")
    @Override
    public int compare(K o1, K o2) {
        return ((Comparable<K>) o1).compareTo(o2);
    }
}
