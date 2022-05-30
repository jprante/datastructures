package org.xbib.datastructures.trie.radix;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node of a Radix tree {@link RadixTreeImpl}
 *
 * @param <T>
 */
class Node<T> {

    private String key;

    private List<Node<T>> children;

    private boolean real;

    private T value;

    public Node() {
        key = "";
        children = new ArrayList<>();
        real = false;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T data) {
        this.value = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String value) {
        this.key = value;
    }

    public boolean isReal() {
        return real;
    }

    public void setReal(boolean datanode) {
        this.real = datanode;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Node<T>> childern) {
        this.children = childern;
    }

    public int getNumberOfMatchingCharacters(String key) {
        int numberOfMatchingCharacters = 0;
        while (numberOfMatchingCharacters < key.length() && numberOfMatchingCharacters < this.getKey().length()) {
            if (key.charAt(numberOfMatchingCharacters) != this.getKey().charAt(numberOfMatchingCharacters)) {
                break;
            }
            numberOfMatchingCharacters++;
        }
        return numberOfMatchingCharacters;
    }

    @Override
    public String toString() {
        return key;
    }
}