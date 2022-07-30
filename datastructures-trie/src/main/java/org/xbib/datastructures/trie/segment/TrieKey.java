package org.xbib.datastructures.trie.segment;

import java.util.List;

public interface TrieKey<T> {

    int size();

    TrieKey<T> subKey(int i);

    TrieKey<T> append(TrieKeySegment<T> trieKeySegment);

    void set(int i, TrieKeySegment<T> trieKeySegment);

    TrieKeySegment<T> getSegment(int i);

    List<TrieKeySegment<T>> getSegments();
}
