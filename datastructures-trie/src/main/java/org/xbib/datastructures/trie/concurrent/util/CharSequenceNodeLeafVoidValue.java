package org.xbib.datastructures.trie.concurrent.util;

import java.util.Collections;
import java.util.List;

/**
 * Stores only incoming edge as a {@link CharSequence} (a <i>view</i> onto the original key) rather than copying the
 * edge into a character array. Returns {@link VoidValue} for the value. Does <b>not</b> store any outgoing edges.
 */
public class CharSequenceNodeLeafVoidValue implements Node {


    // Characters in the edge arriving at this node from a parent node.
    // Once assigned, we never modify this...
    private final CharSequence incomingEdgeCharSequence;

    public CharSequenceNodeLeafVoidValue(CharSequence edgeCharSequence) {
        this.incomingEdgeCharSequence = edgeCharSequence;
    }

    @Override
    public CharSequence getIncomingEdge() {
        return incomingEdgeCharSequence;
    }

    @Override
    public Character getIncomingEdgeFirstCharacter() {
        return incomingEdgeCharSequence.charAt(0);
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
        sb.append("edge=").append(incomingEdgeCharSequence);
        sb.append(", value=").append(VoidValue.SINGLETON);
        sb.append(", edges=[]");
        sb.append("}");
        return sb.toString();
    }
}