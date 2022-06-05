package org.xbib.datastructures.trie.segment;

import java.util.HashMap;
import java.util.Map;

public class NodeImpl<T,V> implements Node<T,V> {

    private TrieKeySegment<T> key;

    private V value;

    private boolean terminal;

    private final Map<TrieKeySegment<T>, Node<T,V>> children;

    public NodeImpl() {
        this.children = new HashMap<>();
    }

    @Override
    public void setKey(TrieKeySegment<T> key) {
        this.key = key;
    }

    @Override
    public TrieKeySegment<T> getKey() {
        return key;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public Map<TrieKeySegment<T>, Node<T,V>> getChildren() {
        return children;
    }
}
