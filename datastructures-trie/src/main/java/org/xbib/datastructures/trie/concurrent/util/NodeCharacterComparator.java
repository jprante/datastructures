package org.xbib.datastructures.trie.concurrent.util;

import java.util.Comparator;

/**
 * Specifies binary search compatibility, and sorting compatibility, of nodes based on
 * {@link Node#getIncomingEdgeFirstCharacter()}.
 */
public class NodeCharacterComparator implements Comparator<NodeCharacterProvider> {

    @Override
    public int compare(NodeCharacterProvider o1, NodeCharacterProvider o2) {
        return o1.getIncomingEdgeFirstCharacter().compareTo(o2.getIncomingEdgeFirstCharacter());
    }
}
