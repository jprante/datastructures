package org.xbib.datastructures.trie.simple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
public class TrieImpl<T> implements Trie<T> {

    private final Node<T> node = new Node<>();

    @Override
    public void add(String key, T value) {
        addNode(node, key, 0, value);
    }

    @Override
    public T search(String key) {
        return findKey(node, key);
    }

    @Override
    public List<T> startsWith(String prefix) {
        List<T> list = new ArrayList<>();
        Node<T> node = this.node;
        for (char c : prefix.toCharArray()) {
            node = node.getChildren().get(c);
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
    public boolean contains(String key) {
        return hasKey(node, key);
    }

    @Override
    public Set<String> getAllKeys() {
        Set<String> keySet = new HashSet<>();
        getKeys(node, "", keySet);
        return keySet;
    }

    @Override
    public int size() {
        return getAllKeys().size();
    }
     
    private void getValues(Node<T> currNode, List<T> valueList) {
        if (currNode.isTerminal()) {
            valueList.add(currNode.getValue());
        }
        Map<Character, Node<T>> children = currNode.getChildren();
        for (Map.Entry<Character, Node<T>> entry : children.entrySet()) {
            getValues(entry.getValue(), valueList);
        }
    }
     
    private void getKeys(Node<T> currNode, String key, Set<String> keySet) {
        if (currNode.isTerminal()) {
            keySet.add(key);
        }
        Map<Character, Node<T>> children = currNode.getChildren();
        for (Map.Entry<Character, Node<T>> entry : children.entrySet()) {
            String s = key + entry.getValue().getKey();
            getKeys(entry.getValue(), s, keySet);
        }
    }
     
    private T findKey(Node<T> currNode, String key) {
        char ch = key.length() > 0 ? key.charAt(0) : '\0';
        if (currNode.getChildren().containsKey(ch)) {
            Node<T> nextNode = currNode.getChildren().get(ch);
            if (key.length() <= 1) {
                if (nextNode.isTerminal()) {
                    return nextNode.getValue();
                }
            } else {
                return findKey(nextNode, key.substring(1));
            }
        }
        return null;
    }
     
    private boolean hasKey(Node<T> currNode, String key) {
        char c = key.length() > 0 ? key.charAt(0) : '\0';
        if (currNode.getChildren().containsKey(c)) {
            Node<T> nextNode = currNode.getChildren().get(c);
            if (key.length() <= 1) {
                return nextNode.isTerminal();
            } else {
                return hasKey(nextNode, key.substring(1));
            }
        }
        return false;
    }
     
    private void addNode(Node<T> currNode, String key, int pos, T value) {
        Character c = pos < key.length() ? key.charAt(pos) : '\0';
        Node<T> nextNode = currNode.getChildren().get(c);
        if (nextNode == null) {
            nextNode = new Node<>();
            nextNode.setKey(c);
            if (pos < key.length() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setValue(value);
                nextNode.setTerminal(true);
            }
            currNode.getChildren().put(c, nextNode);
        } else {
            if (pos < key.length() - 1) {
                addNode(nextNode, key, pos + 1, value);
            } else {
                nextNode.setValue(value);
                nextNode.setTerminal(true);
            }
        }
    }
}
