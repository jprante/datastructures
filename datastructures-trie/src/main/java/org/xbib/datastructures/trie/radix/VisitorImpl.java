package org.xbib.datastructures.trie.radix;

/**
 * A simple standard implementation for a {@link Visitor}.
 */
public abstract class VisitorImpl<T, R> implements Visitor<T, R> {

    protected R result;

    public VisitorImpl() {
        this.result = null;
    }

    public VisitorImpl(R initialValue) {
        this.result = initialValue;
    }

    public R getResult() {
        return result;
    }

    abstract public void visit(String key, Node<T> parent, Node<T> node);
}