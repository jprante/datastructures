package org.xbib.datastructures.trie.concurrent.util;

/**
 * A dummy object which if supplied as a value for an entry in a tree.
 */
public class VoidValue {

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoidValue;
    }

    @Override
    public String toString() {
        return "-";
    }

    VoidValue() {
    }

    public static final VoidValue SINGLETON = new VoidValue();
}
