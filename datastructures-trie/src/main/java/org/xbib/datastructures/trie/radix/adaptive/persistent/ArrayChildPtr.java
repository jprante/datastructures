package org.xbib.datastructures.trie.radix.adaptive.persistent;

class ArrayChildPtr extends ChildPtr {
    private final int i;
    private final Node[] children;

    public ArrayChildPtr(Node[] children, int i) {
        this.children = children;
        this.i = i;
    }

    @Override
    public Node get() {
        return children[i];
    }

    @Override
    public void set(Node n) {
        children[i] = n;
    }
}
