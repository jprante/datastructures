package org.xbib.datastructures.trie.radix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation for Radix tree {@link RadixTree}
 */
public class RadixTreeImpl<T> implements RadixTree<T> {

    protected Node<T> root;

    protected long size;

    public RadixTreeImpl() {
        root = new Node<>();
        root.setKey("");
        size = 0;
    }

    @Override
    public T search(String key) {
        Visitor<T, T> visitor = new VisitorImpl<>() {
            @Override
            public void visit(String key, Node<T> parent, Node<T> node) {
                if (node.isReal()) {
                    result = node.getValue();
                }
            }
        };
        visit(key, visitor);
        return visitor.getResult();
    }

    @Override
    public boolean replace(String key, final T value) {
        Visitor<T, T> visitor = new VisitorImpl<>() {
            public void visit(String key, Node<T> parent, Node<T> node) {
                if (node.isReal()) {
                    node.setValue(value);
                    result = value;
                } else {
                    result = null;
                }
            }
        };
        visit(key, visitor);
        return visitor.getResult() != null;
    }

    @Override
    public boolean delete(String key) {
        Visitor<T, Boolean> visitor = new VisitorImpl<>(Boolean.FALSE) {
            public void visit(String key, Node<T> parent, Node<T> node) {
                result = node.isReal();
                if (result) {
                    if (node.getChildren().size() == 0) {
                        Iterator<Node<T>> it = parent.getChildren().iterator();
                        while (it.hasNext()) {
                            if (it.next().getKey().equals(node.getKey())) {
                                it.remove();
                                break;
                            }
                        }
                        if (parent.getChildren().size() == 1 && !parent.isReal()) {
                            mergeNodes(parent, parent.getChildren().get(0));
                        }
                    } else if (node.getChildren().size() == 1) {
                        mergeNodes(node, node.getChildren().get(0));
                    } else {
                        node.setReal(false);
                    }
                }
            }

            /**
             * Merge a child into its parent node. Operation only valid if it is
             * only child of the parent node and parent node is not a real node.
             *
             * @param parent The parent Node
             * @param child The child Node
             */
            private void mergeNodes(Node<T> parent,
                                    Node<T> child) {
                parent.setKey(parent.getKey() + child.getKey());
                parent.setReal(child.isReal());
                parent.setValue(child.getValue());
                parent.setChildren(child.getChildren());
            }
        };
        visit(key, visitor);
        if (visitor.getResult()) {
            size--;
        }
        return visitor.getResult();
    }

    @Override
    public void insert(String key, T value) throws DuplicateKeyException {
        insert(key, root, value);
        size++;
    }

    /**
     * Recursively insert the key in the radix tree.
     *
     * @param key   The key to be inserted
     * @param node  The current node
     * @param value The value associated with the key
     * @throws DuplicateKeyException If the key already exists in the database.
     */
    private void insert(String key, Node<T> node, T value)
            throws DuplicateKeyException {
        int numberOfMatchingCharacters = node.getNumberOfMatchingCharacters(key);
        if (node.getKey().equals("") || numberOfMatchingCharacters == 0 ||
                (numberOfMatchingCharacters < key.length() && numberOfMatchingCharacters >= node.getKey().length())) {
            boolean flag = false;
            String newText = key.substring(numberOfMatchingCharacters);
            for (Node<T> child : node.getChildren()) {
                if (child.getKey().startsWith(newText.charAt(0) + "")) {
                    flag = true;
                    insert(newText, child, value);
                    break;
                }
            }
            if (!flag) {
                Node<T> n = new Node<>();
                n.setKey(newText);
                n.setReal(true);
                n.setValue(value);
                node.getChildren().add(n);
            }
        } else if (numberOfMatchingCharacters == key.length() && numberOfMatchingCharacters == node.getKey().length()) {
            if (node.isReal()) {
                throw new DuplicateKeyException("Duplicate key");
            }
            node.setReal(true);
            node.setValue(value);
        }
        else if (numberOfMatchingCharacters > 0 && numberOfMatchingCharacters < node.getKey().length()) {
            Node<T> n1 = new Node<>();
            n1.setKey(node.getKey().substring(numberOfMatchingCharacters));
            n1.setReal(node.isReal());
            n1.setValue(node.getValue());
            n1.setChildren(node.getChildren());
            node.setKey(key.substring(0, numberOfMatchingCharacters));
            node.setReal(false);
            node.setChildren(new ArrayList<>());
            node.getChildren().add(n1);
            if (numberOfMatchingCharacters < key.length()) {
                Node<T> n2 = new Node<>();
                n2.setKey(key.substring(numberOfMatchingCharacters));
                n2.setReal(true);
                n2.setValue(value);
                node.getChildren().add(n2);
            } else {
                node.setValue(value);
                node.setReal(true);
            }
        } else {
            Node<T> n = new Node<>();
            n.setKey(node.getKey().substring(numberOfMatchingCharacters));
            n.setChildren(node.getChildren());
            n.setReal(node.isReal());
            n.setValue(node.getValue());
            node.setKey(key);
            node.setReal(true);
            node.setValue(value);
            node.getChildren().add(n);
        }
    }

    @Override
    public List<T> searchPrefix(String key, int recordLimit) {
        List<T> keys = new ArrayList<>();
        Node<T> node = searchPefix(key, root);
        if (node != null) {
            if (node.isReal()) {
                keys.add(node.getValue());
            }
            getNodes(node, keys, recordLimit);
        }
        return keys;
    }

    private void getNodes(Node<T> parent, List<T> keys, int limit) {
        Queue<Node<T>> queue = new LinkedList<>(parent.getChildren());
        while (!queue.isEmpty()) {
            Node<T> node = queue.remove();
            if (node.isReal()) {
                keys.add(node.getValue());
            }
            if (keys.size() == limit) {
                break;
            }
            queue.addAll(node.getChildren());
        }
    }

    private Node<T> searchPefix(String key, Node<T> node) {
        Node<T> result = null;
        int numberOfMatchingCharacters = node.getNumberOfMatchingCharacters(key);
        if (numberOfMatchingCharacters == key.length() && numberOfMatchingCharacters <= node.getKey().length()) {
            result = node;
        } else if (node.getKey().equals("")
                || (numberOfMatchingCharacters < key.length() && numberOfMatchingCharacters >= node.getKey().length())) {
            String newText = key.substring(numberOfMatchingCharacters);
            for (Node<T> child : node.getChildren()) {
                if (child.getKey().startsWith(newText.charAt(0) + "")) {
                    result = searchPefix(newText, child);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean contains(String key) {
        Visitor<T, Boolean> visitor = new VisitorImpl<>(Boolean.FALSE) {
            @Override
            public void visit(String key, Node<T> parent, Node<T> node) {
                result = node.isReal();
            }
        };
        visit(key, visitor);
        return visitor.getResult();
    }

    /**
     * visit the node those key matches the given key
     *
     * @param key     The key that need to be visited
     * @param visitor The visitor object
     */
    public <R> void visit(String key, Visitor<T, R> visitor) {
        if (root != null) {
            visit(key, visitor, null, root);
        }
    }

    /**
     * recursively visit the tree based on the supplied "key". calls the Visitor
     * for the node those key matches the given prefix
     *
     * @param prefix  The key o prefix to search in the tree
     * @param visitor The Visitor that will be called if a node with "key" as its
     *                key is found
     * @param node    The Node from where onward to search
     */
    private <R> void visit(String prefix, Visitor<T, R> visitor,
                           Node<T> parent, Node<T> node) {
        int numberOfMatchingCharacters = node.getNumberOfMatchingCharacters(prefix);
        if (numberOfMatchingCharacters == prefix.length() && numberOfMatchingCharacters == node.getKey().length()) {
            visitor.visit(prefix, parent, node);
        } else if (node.getKey().equals("")
                || (numberOfMatchingCharacters < prefix.length() && numberOfMatchingCharacters >= node.getKey().length())) { // OR we need to
            String newText = prefix.substring(numberOfMatchingCharacters);
            for (Node<T> child : node.getChildren()) {
                if (child.getKey().startsWith(newText.charAt(0) + "")) {
                    visit(newText, visitor, node, child);
                    break;
                }
            }
        }
    }

    @Override
    public long getSize() {
        return size;
    }
}