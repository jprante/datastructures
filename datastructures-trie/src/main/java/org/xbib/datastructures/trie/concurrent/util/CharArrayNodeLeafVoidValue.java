package org.xbib.datastructures.trie.concurrent.util;

import java.util.Collections;
import java.util.List;

/**
 * Stores only incoming edge as a {@code char[]}.
 * Returns {@link VoidValue} for the value. Does <b>not</b> store any outgoing edges.
 */
public class CharArrayNodeLeafVoidValue implements Node {

    // Characters in the edge arriving at this node from a parent node.
    // Once assigned, we never modify this...
    private final char[] incomingEdgeCharArray;

    public CharArrayNodeLeafVoidValue(CharSequence edgeCharSequence) {
        this.incomingEdgeCharArray = CharSequences.toCharArray(edgeCharSequence);
    }

    @Override
    public CharSequence getIncomingEdge() {
        return CharSequences.fromCharArray(incomingEdgeCharArray);
    }

    @Override
    public Character getIncomingEdgeFirstCharacter() {
        return incomingEdgeCharArray[0];
    }

    @Override
    public Object getValue() {
        return VoidValue.SINGLETON;
    }

    @Override
    public Node getOutgoingEdge(Character edgeFirstCharacter) {
        return null;
    }

    @Override
    public void updateOutgoingEdge(Node childNode) {
        throw new IllegalStateException("Cannot update the reference to the following child node for the edge starting with '" + childNode.getIncomingEdgeFirstCharacter() +"', no such edge already exists: " + childNode);
    }

    @Override
    public List<Node> getOutgoingEdges() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node{");
        sb.append("edge=").append(incomingEdgeCharArray);
        sb.append(", value=").append(VoidValue.SINGLETON);
        sb.append(", edges=[]");
        sb.append("}");
        return sb.toString();
    }
}
