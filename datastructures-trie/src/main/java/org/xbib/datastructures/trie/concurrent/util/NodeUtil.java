package org.xbib.datastructures.trie.concurrent.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Static utility methods useful when implementing {@link Node}s.
 */
public class NodeUtil {

    /**
     * Private constructor, not used.
     */
    NodeUtil() {
    }

    /**
     * Returns the index of the node in the given {@link AtomicReferenceArray} whose edge starts with the given
     * first character.
     * <p/>
     * This method expects that some constraints are enforced on the {@link AtomicReferenceArray}:
     * <ul>
     *     <li>
     *         The array must already be in ascending sorted order of the first character of the edge for each node
     *     </li>
     *     <li>
     *         No entries in the array can be null
     *     </li>
     *     <li>
     *         Any existing node in the array cannot be swapped concurrently for another unless the edge associated
     *         with the other node also starts with the same first character
     *     </li>
     * </ul>
     * If these constraints are enforced as expected, then this method will have deterministic behaviour even in the
     * face of concurrent modification.
     *
     * @param childNodes An {@link AtomicReferenceArray} of {@link Node} objects, which is used in accordance with
     * the constraints documented in this method
     *
     * @param edgeFirstCharacter The first character of the edge for which the associated node is required
     * @return The index of the node representing the indicated edge, or a value &lt; 0 if no such node exists in the
     * array
     */
    public static int binarySearchForEdge(AtomicReferenceArray<Node> childNodes, Character edgeFirstCharacter) {
        // inspired by Collections#indexedBinarySearch()
        int low = 0;
        int high = childNodes.length() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Node midVal = childNodes.get(mid);
            int cmp = midVal.getIncomingEdgeFirstCharacter().compareTo(edgeFirstCharacter);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    /**
     * Throws an exception if any nodes in the given list represent edges having the same first character.
     *
     * @param nodes The list of nodes to validate
     * @throws IllegalStateException If a duplicate edge is detected
     */
    public static void ensureNoDuplicateEdges(List<Node> nodes) {
        // Sanity check that no two nodes specify an edge with the same first character...
        Set<Character> uniqueChars = new HashSet<Character>(nodes.size());
        for (Node node : nodes) {
            uniqueChars.add(node.getIncomingEdgeFirstCharacter());
        }
        if (nodes.size() != uniqueChars.size()) {
            throw new IllegalStateException("Duplicate edge detected in list of nodes supplied: " + nodes);
        }
    }
}
