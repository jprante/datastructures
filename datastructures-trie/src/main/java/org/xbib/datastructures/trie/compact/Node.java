package org.xbib.datastructures.trie.compact;

class Node {

    boolean isLeaf;

    char[] value;
    Node[] children;

    // temp variable, carrying information about potential mismatch used for inserting
    int divergeKeyIndex;
    int divergePatternIndex;

    Node copyNode(char[] value) {
        Node node = new Node();
        node.value = value;
        node.isLeaf = this.isLeaf;
        node.children = this.children;
        return node;
    }

    static Node innerNode(char[] value) {
        Node node = new Node();
        node.isLeaf = false;
        node.value = value;
        node.children = new Node[26];
        return node;
    }

    static Node leafNode(char[] value) {
        Node node = new Node();
        node.isLeaf = true;
        node.value = value;
        node.children = new Node[26];
        return node;
    }
}
