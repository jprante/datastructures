package org.xbib.datastructures.json.model;

public abstract class Node {

    private final int depth;

    public Node(Node parent) {
        this.depth = parent != null ? parent.depth + 1 : 0;
    }

    public int getDepth() {
        return depth;
    }
}
