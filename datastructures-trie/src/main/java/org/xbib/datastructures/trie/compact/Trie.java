package org.xbib.datastructures.trie.compact;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/467046/Java-Radix-tree-(compact-prefix-tree)-beats-99.7-runtime-and-100-memory
 */

public class Trie {

    private static int indexOf(char c) {
        return c - 'a';
    }

    private final Node root;

    public Trie() {
        root = Node.innerNode(new char[0]);
    }

    public void insert(String word) {
        char[] key = word.toCharArray();
        Node lastNode = search(root, key, 0);
        if (lastNode.divergeKeyIndex == key.length) { 
            if (lastNode.divergePatternIndex == lastNode.value.length) {
                lastNode.isLeaf = true;
            } else {
                // we need to reduce length of the compressed pattern in the current node,
                // make it node leaf, and create child that carry over the original children/isLeaf
                char[] childValue = Arrays.copyOfRange(lastNode.value,
                        lastNode.divergePatternIndex, lastNode.value.length);
                Node childNode = lastNode.copyNode(childValue);
                lastNode.value = Arrays.copyOfRange(lastNode.value,
                        0, lastNode.divergePatternIndex);
                lastNode.isLeaf = true;
                lastNode.children = new Node[26];
                lastNode.children[indexOf(childValue[0])] = childNode;
            }
        } else { 
            if (lastNode.divergePatternIndex < lastNode.value.length) {// If diverge happens in middle of both array
                // we need to reduce length of the compressed pattern in the current node
                // create one leaf node for the new key, one child that carry over the original children/isLeaf
                Node newLeaf = Node.leafNode(Arrays.copyOfRange(key, lastNode.divergeKeyIndex, key.length));
                Node newChild = lastNode.copyNode(Arrays.copyOfRange(lastNode.value,
                        lastNode.divergePatternIndex, lastNode.value.length));
                lastNode.children = new Node[26];
                lastNode.children[indexOf(newLeaf.value[0])] = newLeaf;
                lastNode.children[indexOf(newChild.value[0])] = newChild;
                lastNode.isLeaf = false;
                lastNode.value = Arrays.copyOfRange(lastNode.value, 0, lastNode.divergePatternIndex);
            } else { // pattern is shorter than the key
                // we need to create a left node for the new key, and append it to the original ndoe
                Node newLeaf = Node.leafNode(Arrays.copyOfRange(key, lastNode.divergeKeyIndex, key.length));
                lastNode.children[indexOf(newLeaf.value[0])] = newLeaf;
            }
        }
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        char[] key = word.toCharArray();
        Node lastNode = search(root, key, 0);
        // if we run out of key and pattern, and node is leaf, then key exists
        return lastNode.divergeKeyIndex == key.length &&
                lastNode.divergePatternIndex == lastNode.value.length &&
                lastNode.isLeaf;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        char[] key = prefix.toCharArray();
        Node lastNode = search(root, key, 0);
        // if we run out of key, than prefix exists
        return lastNode.divergeKeyIndex == key.length;
    }

    // Return the last node in the search path
    private Node search(Node node, char[] key, int begin) {
        int patternMismatchIndex = compare(key, begin, node.value);
        int divergeKeyIndex = patternMismatchIndex + begin;

        if (divergeKeyIndex >= key.length) {// If we run out of keys (could be match or prefix)
            node.divergeKeyIndex = divergeKeyIndex;
            node.divergePatternIndex = patternMismatchIndex;
            return node;
        } else {
            if (patternMismatchIndex >= node.value.length) {// if we run out of values(continue search child)
                Node nextNode = node.children[indexOf(key[divergeKeyIndex])];
                if (nextNode == null) { // we've reach the end
                    node.divergeKeyIndex = divergeKeyIndex;
                    node.divergePatternIndex = patternMismatchIndex;
                    return node;
                } else {
                    return search(nextNode, key, divergeKeyIndex);
                }
            } else {// if mismatch happens in the middle of pattern
                node.divergeKeyIndex = divergeKeyIndex;
                node.divergePatternIndex = patternMismatchIndex;
                return node;
            }
        }
    }

    private static int compare(char[] src, int begin, char[] pattern) {
        int srcIndex = begin;
        int patternIndex = 0;
        while (srcIndex < src.length && patternIndex < pattern.length) {
            if (src[srcIndex] != pattern[srcIndex - begin]) {
                break; // src and pattern diverges
            }
            srcIndex ++;
            patternIndex ++;
        }
        return patternIndex;
    }
}