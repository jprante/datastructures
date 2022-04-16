package org.xbib.datastructures.trie.radix;

/**
 * The visitor interface that is used by {@link RadixTreeImpl} for perfroming
 * task on a searched node.
 */
public interface Visitor<T, R> {
    /**
     * This method gets called by {@link RadixTreeImpl#visit(String, Visitor) visit}
     * when it finds a node matching the key given to it.
     *
     * @param key    The key that matched the node
     * @param parent The parent of the node being visited
     * @param node   The node that is being visited
     */
    void visit(String key, Node<T> parent, Node<T> node);

    /**
     * The visitor can store any type of result object, depending on the context of
     * what it is being used for.
     *
     * @return The result captured by the visitor.
     */
    R getResult();
}
