package org.xbib.datastructures.trie;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WordTreeTest {

    static class Node {

        private final char ch;

        private boolean leaf;

        private LinkedList<Node> children;

        public Node(char ch) {
            this.ch = ch;
        }

        public void addChild(Node node) {
            if (children == null) {
                children = new LinkedList<>();
            }
            children.add(node);
        }

        public Node getNode(char ch) {
            if (children == null) {
                return null;
            }
            for (Node child : children) {
                if (child.getChar() == ch) {
                    return child;
                }
            }
            return null;
        }

        public char getChar() {
            return ch;
        }

        public List<Node> getChildren() {
            return Objects.requireNonNullElse(this.children, Collections.emptyList());
        }

        public boolean isLeaf() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }
    }

    Node root = new Node(' ');

    public WordTreeTest() {
    }

    public List<String> getWordsForPrefix(String prefix) {
        if (prefix.length() == 0) {
            return Collections.emptyList();
        }
        Node node = getNodeForPrefix(root, prefix);
        if (node == null) {
            return Collections.emptyList();
        }
        List<LinkedList<Character>> chars = collectChars(node);
        List<String> words = new ArrayList<>(chars.size());
        for (List<Character> charList : chars) {
            words.add(combine(prefix.substring(0, prefix.length() - 1), charList));
        }
        return words;
    }


    private String combine(String prefix, List<Character> charList) {
        StringBuilder sb = new StringBuilder(prefix);
        for (Character character : charList) {
            sb.append(character);
        }
        return sb.toString();
    }

    private Node getNodeForPrefix(Node node, String prefix) {
        if (prefix.length() == 0) {
            return node;
        }
        Node next = node.getNode(prefix.charAt(0));
        if (next == null) {
            return null;
        }
        return getNodeForPrefix(next, prefix.substring(1));
    }

    private List<LinkedList<Character>> collectChars(Node node) {
        List<LinkedList<Character>> chars = new ArrayList<>();
        if (node.getChildren().size() == 0) {
            chars.add(new LinkedList<>(Collections.singletonList(node.getChar())));
        } else {
            if (node.isLeaf()) {
                chars.add(new LinkedList<>(Collections.singletonList(node.getChar())));
            }
            List<Node> children = node.getChildren();
            for (Node child : children) {
                List<LinkedList<Character>> childList = collectChars(child);
                for (LinkedList<Character> characters : childList) {
                    characters.push(node.getChar());
                    chars.add(characters);
                }
            }
        }
        return chars;
    }


    public void addWord(String word) {
        addWord(root, word);
    }

    private void addWord(Node parent, String word) {
        if (word.trim().length() == 0) {
            return;
        }
        Node child = parent.getNode(word.charAt(0));
        if (child == null) {
            child = new Node(word.charAt(0));
            parent.addChild(child);
        }
        if (word.length() == 1) {
            child.setLeaf(true);
        } else {
            addWord(child, word.substring(1));
        }
    }

    @Test
    public void testWordTree() {
        WordTreeTest tree = new WordTreeTest();
        tree.addWord("world");
        tree.addWord("work");
        tree.addWord("wolf");
        tree.addWord("life");
        tree.addWord("love");
        System.out.println(tree.getWordsForPrefix("wo"));
    }
}
