package org.xbib.datastructures.trie.segment;

import java.util.List;
import java.util.Set;

public interface Trie<T,K extends TrieKey<T>, V> {

    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);

    Set<K> getKeys();

    int size();

    List<V> startsWith(List<TrieKeySegment<T>> prefix);

}
