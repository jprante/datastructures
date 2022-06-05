package org.xbib.datastructures.trie.segment;

import java.util.Map;

public interface Node<T, V> {

    void setKey(TrieKeySegment<T> key);

    TrieKeySegment<T> getKey();

    void setValue(V value);

    V getValue();

    void setTerminal(boolean terminal);

    boolean isTerminal();

    Map<TrieKeySegment<T>, Node<T,V>> getChildren();
}
