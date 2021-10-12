package org.xbib.datastructures.yaml.tiny.test;

import org.xbib.datastructures.yaml.tiny.MapNode;
import org.xbib.datastructures.yaml.tiny.ListNode;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.yaml.tiny.ValueNode;

import java.util.List;
import java.util.Map;

public class Formatter {

    public String format(Node<?> node) {
        if (node instanceof ValueNode) {
            return format((ValueNode) node);
        }
        if (node instanceof ListNode) {
            return format((ListNode) node);
        }
        if (node instanceof MapNode) {
            return format((MapNode) node);
        }
        return "";
    }

    private String format(ValueNode valueNode) {
        String indent = spaces(valueNode.getDepth());
        StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append("<TextNode value=");
        if (valueNode.get() != null) {

            String s = excerpt(valueNode.get());
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
        if (!listNode.get().isEmpty()) {
            sb.append("[");
            for (int i = 0; i < listNode.get().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(i).append(": ").append(toStringOfComments(listNode, i))
                        .append('\n').append(format(listNode.get().get(i)));
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

    private String format(MapNode mapNode) {
        StringBuilder sb = new StringBuilder();
        String indent = spaces(mapNode.getDepth());
        sb.append(indent).append("<HashNode children=");
        if (!mapNode.get().isEmpty()) {
            sb.append('{');
            boolean isFirst = true;
            for (Map.Entry<CharSequence, Node<?>> kv : mapNode.get().entrySet()) {
                if (!isFirst) {
                    sb.append(", ");
                }
                sb.append('"').append(kv.getKey()).append("\": ")
                        .append(toStringOfComments(mapNode, kv.getKey())).append('\n')
                        .append(format(kv.getValue()));
                isFirst = false;
            }
            sb.append('\n').append(indent).append('}');
        }
        sb.append(">");
        return sb.toString();
    }

    private String toStringOfComments(MapNode mapNode, CharSequence charSequence) {
        StringBuilder sb = new StringBuilder();
        List<String> comments = mapNode.getComments().get(charSequence.toString());
        if (comments != null && !comments.isEmpty()) {
            sb.append("comments=");
            StringBuilder commentStr = new StringBuilder();
            for (String comment : comments) {
                commentStr.append(comment).append(' ');
            }
            String s = excerpt(commentStr.toString());
            s = quote(s, true);
            sb.append(s);
        }
        return sb.toString();
    }

    private String excerpt(Object object) {
        if (object == null) {
            return "null";
        }
        String string = object.toString();
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
