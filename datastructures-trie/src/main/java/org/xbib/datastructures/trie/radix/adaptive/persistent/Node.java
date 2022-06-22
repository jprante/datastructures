package org.xbib.datastructures.trie.radix.adaptive.persistent;

abstract class Node {
    static final int MAX_PREFIX_LEN = 8;
    int refcount;

    public Node() {
        refcount = 0;
    }

    public static Node n_clone(Node n) {
        if (n == null) return null;
        else return n.n_clone();
    }

    public static Leaf minimum(Node n) {
        if (n == null) return null;
        else return n.minimum();
    }

    public static boolean insert(Node n, ChildPtr ref, final byte[] key, Object value, int depth,
                                 boolean force_clone) {
        // If we are at a NULL node, inject a leaf
        if (n == null) {
            ref.change(new Leaf(key, value));
            return true;
        } else {
            return n.insert(ref, key, value, depth, force_clone);
        }
    }

    public static boolean exhausted(Node n, int i) {
        if (n == null) return true;
        else return n.exhausted(i);
    }

    static int to_uint(byte b) {
        return ((int) b) & 0xFF;
    }

    public abstract Node n_clone();

    public abstract Leaf minimum();

    public abstract boolean insert(ChildPtr ref, final byte[] key, Object value, int depth,
                                   boolean force_clone) throws UnsupportedOperationException;

    public abstract boolean delete(ChildPtr ref, final byte[] key, int depth,
                                   boolean force_clone);

    public abstract int decrement_refcount();

    public abstract boolean exhausted(int i);
}
