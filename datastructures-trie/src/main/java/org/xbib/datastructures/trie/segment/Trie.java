package org.xbib.datastructures.trie.segment;

import java.util.List;
import java.util.Set;

public interface Trie<T,K extends TrieKey<T>, V> {

    void add(K key, V value);

    V search(K key);

    List<V> startsWith(List<TrieKeySegment<T>> prefix);

    boolean contains(K key);

    Set<K> getAllKeys();

    int size();
}
