package org.xbib.datastructures.yaml.test;

import org.xbib.datastructures.yaml.model.HashNode;
import org.xbib.datastructures.yaml.model.ListNode;
import org.xbib.datastructures.yaml.model.Node;
import org.xbib.datastructures.yaml.model.ValueNode;
import java.util.Map;

public class Formatter {

    public String format(Node node) {
        if (node instanceof ValueNode) {
            return format((ValueNode) node);
        }
        if (node instanceof ListNode) {
            return format((ListNode) node);
        }
        if (node instanceof HashNode) {
            return format((HashNode) node);
        }
        return "";
    }

    private String format(ValueNode valueNode) {
        String indent = spaces(valueNode.getDepth());
        StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append("<TextNode value=");
        if (valueNode.getValue() != null) {
            String s = excerpt(valueNode.getValue());
            s = escape(s);
            s = quote(s, true);
            sb.append(s);
        }
        sb.append(toStringOfComments(valueNode));
        sb.append('>');
        return sb.toString();
    }

    private String toStringOfComments(ValueNode valueNode) {
        String indent = spaces(valueNode.getDepth());
        StringBuilder sb = new StringBuilder();
        if (valueNode.getComments() != null && !valueNode.getComments().isEmpty()) {
            sb.append('\n');
            sb.append(indent);
            sb.append(spaces(10));
            sb.append("comments=");
            StringBuilder stringBuilder = new StringBuilder();
            for (String comment : valueNode.getComments()) {
                stringBuilder.append(comment);
                stringBuilder.append(' ');
            }
            String s = excerpt(stringBuilder.toString(), 40);
            s = quote(s, true);
            sb.append(s);
        }
        return sb.toString();
    }

    private String format(ListNode listNode) {
        String indent = spaces(listNode.getDepth());
        StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append("<ListNode items=");
        if (!listNode.getItems().isEmpty()) {
            sb.append("[");
            for (int i = 0; i < listNode.getItems().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(i).append(": ").append(toStringOfComments(listNode, i))
                        .append('\n').append(format(listNode.getItems().get(i)));
            }
            sb.append('\n');
            sb.append(indent);
            sb.append(']');
        }
        sb.append(">");
        return sb.toString();
    }

    private String toStringOfComments(ListNode listNode, int index) {
        StringBuilder sb = new StringBuilder();
        if (listNode.getComments().size() > index &&
                listNode.getComments().get(index) != null &&
                !listNode.getComments().get(index).isEmpty()) {
            sb.append("comments=");
            StringBuilder comments = new StringBuilder();
            for (String comment : listNode.getComments().get(index)) {
                comments.append(comment);
                comments.append(' ');
            }
            String s = excerpt(comments.toString());
            s = quote(s, true);
            sb.append(s);
        }
        return sb.toString();
    }

    private String format(HashNode hashNode) {
        StringBuilder sb = new StringBuilder();
        String indent = spaces(hashNode.getDepth());
        sb.append(indent).append("<HashNode children=");
        if (!hashNode.getChildren().isEmpty()) {
            sb.append('{');
            boolean isFirst = true;
            for (Map.Entry<String, Node> kv : hashNode.getChildren().entrySet()) {
                if (!isFirst) {
                    sb.append(", ");
                }
                sb.append('"').append(kv.getKey()).append("\": ")
                        .append(toStringOfComments(hashNode, kv.getKey())).append('\n')
                        .append(format(kv.getValue()));
                isFirst = false;
            }
            sb.append('\n').append(indent).append('}');
        }
        sb.append(">");
        return sb.toString();
    }

    private String toStringOfComments(HashNode hashNode, String name) {
        StringBuilder sb = new StringBuilder();
        if (hashNode.getComments().get(name) != null && !hashNode.getComments().get(name).isEmpty()) {
            sb.append("comments=");
            StringBuilder comments = new StringBuilder();
            for (String comment : hashNode.getComments().get(name)) {
                comments.append(comment).append(' ');
            }
            String s = excerpt(comments.toString());
            s = quote(s, true);
            sb.append(s);
        }
        return sb.toString();
    }

    private String excerpt(String string) {
        int len = string.length();
        if (len <= 24) {
            return excerpt(string, 24);
        } else if (len <= 80) {
            return excerpt(string, len / 3);
        } else if (len <= 160) {
            return excerpt(string, 100);
        }
        return excerpt(string,150);
    }

    private String excerpt(String string, int count) {
        if (string.length() > count) {
            string = string.substring(0, count) + "...";
        }
        return string;
    }

    private String escape(String string) {
        return string.replace("\0", "\\0")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\\", "\\\\");
    }

    private String quote(String string, boolean singleQuote) {
        return singleQuote ? "'" + string + "'" : "\"" + string + "\"";
    }

    private String spaces(int count) {
        return " ".repeat(count);
    }

}
