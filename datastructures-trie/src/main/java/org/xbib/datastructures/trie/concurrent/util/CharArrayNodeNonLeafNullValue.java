package org.xbib.datastructures.trie.concurrent.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Stores incoming edge as a {@code char[]} and outgoing edges as an {@link AtomicReferenceArray}. Does not store a
 * value and returns {@code null} for the value.
 */
public class CharArrayNodeNonLeafNullValue implements Node {


    // Characters in the edge arriving at this node from a parent node.
    // Once assigned, we never modify this...
    private final char[] incomingEdgeCharArray;

    // References to child nodes representing outgoing edges from this node.
    // Once assigned we never add or remove references, but we do update existing references to point to new child
    // nodes provided new edges start with the same first character...
    private final AtomicReferenceArray<Node> outgoingEdges;

    // A read-only List wrapper around the outgoingEdges AtomicReferenceArray...
    private final List<Node> outgoingEdgesAsList;

    public CharArrayNodeNonLeafNullValue(CharSequence edgeCharSequence, List<Node> outgoingEdges) {
        Node[] childNodeArray = outgoingEdges.toArray(new Node[outgoingEdges.size()]);
        // Sort the child nodes...
        Arrays.sort(childNodeArray, new NodeCharacterComparator());
        this.outgoingEdges = new AtomicReferenceArray<Node>(childNodeArray);
        this.incomingEdgeCharArray = CharSequences.toCharArray(edgeCharSequence);
        this.outgoingEdgesAsList = new AtomicReferenceArrayListAdapter<Node>(this.outgoingEdges);
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
        return null;
    }

    @Override
    public Node getOutgoingEdge(Character edgeFirstCharacter) {
        // Binary search for the index of the node whose edge starts with the given character.
        // Note that this binary search is safe in the face of concurrent modification due to constraints
        // we enforce on use of the array, as documented in the binarySearchForEdge method...
        int index = NodeUtil.binarySearchForEdge(outgoingEdges, edgeFirstCharacter);
        if (index < 0) {
            // No such edge exists...
            return null;
        }
        // Atomically return the child node at this index...
        return outgoingEdges.get(index);
    }

    @Override
    public void updateOutgoingEdge(Node childNode) {
        // Binary search for the index of the node whose edge starts with the given character.
        // Note that this binary search is safe in the face of concurrent modification due to constraints
        // we enforce on use of the array, as documented in the binarySearchForEdge method...
        int index = NodeUtil.binarySearchForEdge(outgoingEdges, childNode.getIncomingEdgeFirstCharacter());
        if (index < 0) {
            throw new IllegalStateException("Cannot update the reference to the following child node for the edge starting with '" + childNode.getIncomingEdgeFirstCharacter() +"', no such edge already exists: " + childNode);
        }
        // Atomically update the child node at this index...
        outgoingEdges.set(index, childNode);
    }

    @Override
    public List<Node> getOutgoingEdges() {
        return outgoingEdgesAsList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node{");
        sb.append("edge=").append(incomingEdgeCharArray);
        sb.append(", value=null");
        sb.append(", edges=").append(getOutgoingEdges());
        sb.append("}");
        return sb.toString();
    }
}
