package org.xbib.datastructures.trie.segment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrieImpl<T,V> implements Trie<T, TrieKey<T>, V> {

    private final Node<T,V> node;

    public TrieImpl() {
        this.node = new NodeImpl<>();
    }

    @Override
    public void add(TrieKey<T> key, V value) {
        addNode(node, key, 0, value);
    }

    @Override
    public V search(TrieKey<T> key) {
        return findKey(node, key);
    }

    @Override
    public List<V> startsWith(List<TrieKeySegment<T>> prefix) {
        List<V> list = new ArrayList<>();
        Node<T,V> node = this.node;
        for (TrieKeySegment<T> e : prefix) {
            node = node.getChildren().get(e);
            if (node == null) {
                break;
            }
        }
        if (node != null) {
            getValues(node, list);
        }
        return list;
    }

    @Override
    public boolean contains(TrieKey<T> key) {
        return hasKey(node, key);
    }

    @Override
    public Set<TrieKey<T>> getAllKeys() {
        Set<TrieKey<T>> keySet = new HashSet<>();
        getKeys(node, new TrieKeyImpl<>(), keySet);
        return keySet;
    }

    @Override
    public int size() {
        return getAllKeys().size();
    }
     
    private void getValues(Node<T,V> currNode, List<V> valueList) {
        if (currNode.isTerminal()) {
            valueList.add(currNode.getValue());
        }
        Map<TrieKeySegment<T>, Node<T,V>> children = currNode.getChildren();
        for (Map.Entry<TrieKeySegment<T>, Node<T,V>> entry : children.entrySet()) {
            getValues(entry.getValue(), valueList);
        }
    }
     
    private void getKeys(Node<T,V> currNode, TrieKey<T> key, Set<TrieKey<T>> keySet) {
        if (currNode.isTerminal()) {
            keySet.add(key);
        }
        Map<TrieKeySegment<T>, Node<T,V>> children = currNode.getChildren();
        for (Map.Entry<TrieKeySegment<T>, Node<T,V>> entry : children.entrySet()) {
            TrieKey<T> k = key.append(entry.getValue().getKey());
            getKeys(entry.getValue(), k, keySet);
        }
    }
     
    private V findKey(Node<T,V> currNode, TrieKey<T> key) {
        TrieKeySegment<T> e = key.size() > 0 ? key.get(0) : null;
        if (currNode.getChildren().containsKey(e)) {
            Node<T,V> nextNode = currNode.getChildren().get(e);
            if (key.size() <= 1) {
                if (nextNode.isTerminal()) {
                    return nextNode.getValue();
                }
            } else {
                return findKey(nextNode, key.subKey(1));
            }
        }
        return null;
    }
     
    private boolean hasKey(Node<T,V> currNode, TrieKey<T> key) {
        TrieKeySegment<T> e = key.size() > 0 ? key.get(0) : null;
        if (currNode.getChildren().containsKey(e)) {
            Node<T,V> nextNode = currNode.getChildren().get(e);
            if (key.size() <= 1) {
                return nextNode.isTerminal();
            } else {
                return hasKey(nextNode, key.subKey(1));
            }
        }
        return false;
    }
     
    private void addNode(Node<T,V> currNode, TrieKey<T> key, int pos, V value) {
        TrieKeySegment<T> e = pos < key.size() ? key.get(pos) : null;
        Node<T,V> nextNode = currNode.getChildren().get(e);
        if (nextNode == null) {
            nextNode = new NodeImpl<>();
            nextNode.setKey(e);
            if (pos < key.size() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setValue(value);
                nextNode.setTerminal(true);
            }
            currNode.getChildren().put(e, nextNode);
        } else {
            if (pos < key.size() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setValue(value);
                nextNode.setTerminal(true);
            }
        }
    }
}
