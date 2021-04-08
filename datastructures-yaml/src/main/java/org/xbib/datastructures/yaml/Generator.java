package org.xbib.datastructures.yaml;

import org.xbib.datastructures.yaml.model.HashNode;
import org.xbib.datastructures.yaml.model.ListNode;
import org.xbib.datastructures.yaml.model.Node;
import org.xbib.datastructures.yaml.model.ValueNode;
import org.xbib.datastructures.yaml.model.ValueNode.TextType;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class Generator {

    private final Node root;

    private final int indent;

    public Generator(Node root) {
        this(root, 2);
    }

    public Generator(Node root, int indent) {
        this.root = root;
        this.indent = indent;
    }

    public void generate(Writer writer) throws IOException {
        try (writer) {
            if (root != null) {
                if (internalWrite(writer, root, null)) {
                    writer.append('\n');
                }
            }
        }
    }

    private boolean internalWrite(Appendable appendable, Node curnode, Node prevnode) throws IOException {
        if (curnode == null) {
            return false;
        }
        boolean lf = false;
        String indent = " ".repeat(curnode.getDepth() * this.indent);
        if (curnode instanceof ValueNode) {
            ValueNode txnode = (ValueNode) curnode;
            lf = writeComments(appendable, txnode.getComments(), indent, false);
            switch (txnode.getType()) {
                case LINE:
                    if (lf) {
                        appendable.append('\n').append(indent);
                    }
                    appendable.append(txnode.getValue());
                    break;
                case MULTILINE:
                    if (lf) {
                        appendable.append('\n');
                    }
                    String s = prefix(linewrap(txnode.getValue()), indent);
                    appendable.append(s);
                    break;
                case TEXT:
                case TEXT_ANGLE:
                    if (txnode.getType() == TextType.TEXT) {
                        appendable.append("|\n");
                    }
                    if (txnode.getType() == TextType.TEXT_ANGLE) {
                        appendable.append(">\n");
                    }
                    appendable.append(prefix(txnode.getValue(), indent));
                    break;
            }
            return true;
        } else if (curnode instanceof HashNode) {
            HashNode hashNode = (HashNode) curnode;
            for (Map.Entry<String, Map.Entry<Node, List<String>>> knc : hashNode.getChildCommentPairs()) {
                lf = writeComments(appendable, knc.getValue().getValue(), indent, lf);
                if (lf || (prevnode != null && !(prevnode instanceof ListNode))) {
                    appendable.append("\n").append(indent);
                }
                appendable.append(knc.getKey()).append(": ");
                lf = internalWrite(appendable, knc.getValue().getKey(), hashNode);
            }
            return lf;
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            for (Map.Entry<Node, List<String>> nc : listNode.getItemCommentPairs()) {
                lf = writeComments(appendable, nc.getValue(), indent, lf);
                if (lf || (prevnode != null && !(prevnode instanceof ListNode))) {
                    appendable.append("\n").append(indent);
                }
                appendable.append("- ");
                lf = internalWrite(appendable, nc.getKey(), listNode);
            }
            return lf;
        }
        return false;
    }

    private boolean writeComments(Appendable appendable, List<String> comments, String indent, boolean lf) throws IOException {
        if (comments == null) {
            return false;
        }
        for (String comment : comments) {
            if (lf) {
                appendable.append('\n');
            }
            appendable.append(indent).append('#').append(comment);
            lf = true;
        }
        return lf;
    }

    private static String prefix(String string, String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return string;
        }
        StringBuilder sb = new StringBuilder();
        String[] lines = string.replace("\r", "").split("\n");
        for (int i = 0; i < lines.length; i++) {
            sb.append(prefix);
            sb.append(lines[i]);
            if (i != lines.length - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private static String linewrap(String string) {
        StringBuilder sb = new StringBuilder();
        int len = 0;
        String[] segments = string.split("[ \n\r\t]");
        for (int i = 0; i < segments.length; i++) {
            while (i < segments.length && len + segments[i].length() <= 64) {
                sb.append(segments[i]);
                sb.append(' ');
                len += segments[i].length();
                i++;
            }
            sb.deleteCharAt(sb.length() - 1);
            len = 0;
            if (i != segments.length - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

}
