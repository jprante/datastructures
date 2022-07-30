package org.xbib.datastructures.trie.concurrent.util;

import java.util.Collections;
import java.util.List;

/**
 * Stores only incoming edge as a {@code char[]}, and a reference to a value. Does <b>not</b> store any outgoing
 * edges.
 */
public class CharArrayNodeLeafWithValue implements Node {


    // Characters in the edge arriving at this node from a parent node.
    // Once assigned, we never modify this...
    private final char[] incomingEdgeCharArray;

    // An arbitrary value which the application associates with a key matching the path to this node in the tree.
    // This value can be null...
    private final Object value;

    public CharArrayNodeLeafWithValue(CharSequence edgeCharSequence, Object value) {
        this.incomingEdgeCharArray = CharSequences.toCharArray(edgeCharSequence);
        this.value = value;
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
        return value;
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
        sb.append(", value=").append(value);
        sb.append(", edges=[]");
        sb.append("}");
        return sb.toString();
    }
}
