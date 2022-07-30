package org.xbib.datastructures.trie.concurrent;

import org.xbib.datastructures.trie.concurrent.util.Node;

import java.io.IOException;
import java.util.List;

/**
 * Utility methods to generate semi-graphical string representations of trees.
 */
public class PrettyPrinter {

    /**
     * Private constructor, not used.
     */
    PrettyPrinter() {
    }

    /**
     * Generates a semi-graphical string representation of a given tree.
     * <p/>
     * Example output:<br/>
     * <pre>
     * ○
     * └── ○ B (1)
     *     └── ○ A (2)
     *         └── ○ N (3)
     *             ├── ○ AN (5)
     *             │   └── ○ A (6)
     *             └── ○ DANA (4)
     * </pre>
     *
     * @param tree The tree for which the semi-graphical representation should be generated
     * @return A semi-graphical string representation of the tree
     */
    public static String prettyPrint(ConcurrentRadixTree<?> tree) {
        return prettyPrint(tree.getNode());
    }

    public static String prettyPrint(Node node) {
        StringBuilder sb = new StringBuilder();
        prettyPrint(node, sb, "", true, true);
        return sb.toString();
    }

    /**
     * Generates a semi-graphical string representation of a given tree, writing it to a given {@link Appendable}.
     * <p/>
     * Example output:<br/>
     * <pre>
     * ○
     * └── ○ B (1)
     *     └── ○ A (2)
     *         └── ○ N (3)
     *             ├── ○ AN (5)
     *             │   └── ○ A (6)
     *             └── ○ DANA (4)
     * </pre>
     *
     * @param tree The tree for which the semi-graphical representation should be generated
     * @param appendable The object to which the tree should be written
     */
    public static void prettyPrint(ConcurrentRadixTree<?> tree, Appendable appendable) {
        prettyPrint(tree.getNode(), appendable, "", true, true);
    }

    static void prettyPrint(Node node, Appendable sb, String prefix, boolean isTail, boolean isRoot) {
        try {
            StringBuilder label = new StringBuilder();
            if (isRoot) {
                label.append("○");
                if (node.getIncomingEdge().length() > 0) {
                    label.append(" ");
                }
            }
            label.append(node.getIncomingEdge());
            if (node.getValue() != null) {
                label.append(" (").append(node.getValue()).append(")");
            }
            sb.append(prefix).append(isTail ? isRoot ? "" : "└── ○ " : "├── ○ ").append(label).append("\n");
            List<Node> children = node.getOutgoingEdges();
            for (int i = 0; i < children.size() - 1; i++) {
                prettyPrint(children.get(i), sb, prefix + (isTail ? isRoot ? "" : "    " : "│   "), false, false);
            }
            if (!children.isEmpty()) {
                prettyPrint(children.get(children.size() - 1), sb, prefix + (isTail ? isRoot ? "" : "    " : "│   "), true, false);
            }
        }
        catch (IOException ioException) {
            // Rethrow the checked exception as a runtime exception...
            throw new IllegalStateException(ioException);
        }
    }
}
