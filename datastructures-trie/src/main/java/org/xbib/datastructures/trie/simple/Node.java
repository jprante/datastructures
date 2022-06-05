package org.xbib.datastructures.trie.simple;

import java.util.HashMap;
import java.util.Map;
 
public class Node<T> {

    private Character key;

    private T value;

    private boolean terminal;

    private final Map<Character, Node<T>> children = new HashMap<>();
     
    public Character getKey() {
        return key;
    }
 
    public void setKey(Character key) {
        this.key = key;
    }
 
    public T getValue() {
        return value;
    }
 
    public void setValue(T value) {
        this.value = value;
    }
     
    public boolean isTerminal() {
        return terminal;
    }
 
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
 
    public Map<Character, Node<T>> getChildren() {
        return children;
    }
}
