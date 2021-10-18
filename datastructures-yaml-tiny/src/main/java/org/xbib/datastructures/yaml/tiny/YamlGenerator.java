package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyList;
import org.xbib.datastructures.tiny.TinyMap;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class YamlGenerator implements Generator  {

    private final Node<?> root;

    private final int indent;

    private Writer writer;

    public YamlGenerator(Node<?> root) {
        this(root, 2, new StringWriter());
    }

    public YamlGenerator(Node<?> root, int indent, Writer writer) {
        this.root = root;
        this.writer = writer;
        this.indent = indent;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Node<?> root) {
        return (Map<String, Object>) internalMap(root);
    }

    @Override
    public void generate(Writer writer) throws IOException {
        this.writer = writer;
        try (writer) {
            if (root != null) {
                if (internalWrite(root, null)) {
                    writer.append('\n');
                }
            }
        }
    }

    private boolean internalWrite(Node<?> curnode, Node<?> prevnode) throws IOException {
        if (curnode == null) {
            return false;
        }
        boolean lf = false;
        String indent = " ".repeat(curnode.getDepth() * this.indent);
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            lf = writeComments(valueNode.getComments(), indent, false);
            switch (valueNode.getType()) {
                case LINE: {
                    if (lf) {
                        writer.append('\n').append(indent);
                    }
                    Object object = valueNode.get();
                    String s = object == null ? "null" : object.toString();
                    writer.append(s.isEmpty() ? "\"\"" : s);
                    break;
                }
                case MULTILINE: {
                    if (lf) {
                        writer.append('\n');
                    }
                    Object object = valueNode.get();
                    String s = object == null ? "null" : object.toString();
                    writer.append(prefix(linewrap(s), indent));
                    break;
                }
                case TEXT:
                case TEXT_ANGLE:
                    if (valueNode.getType() == ValueNode.TextType.TEXT) {
                        writer.append("|\n");
                    }
                    if (valueNode.getType() == ValueNode.TextType.TEXT_ANGLE) {
                        writer.append(">\n");
                    }
                    Object object = valueNode.get();
                    String s = object == null ? "null" : object.toString();
                    writer.append(prefix(s, indent));
                    break;
            }
            return true;
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            for (Map.Entry<CharSequence, Map.Entry<Node<?>, List<String>>> knc : mapNode.getChildCommentPairs()) {
                lf = writeComments(knc.getValue().getValue(), indent, lf);
                if (lf || (prevnode != null && !(prevnode instanceof ListNode))) {
                    writer.append("\n").append(indent);
                }
                writer.append(knc.getKey()).append(": ");
                lf = internalWrite(knc.getValue().getKey(), mapNode);
            }
            return lf;
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            for (Map.Entry<Node<?>, List<String>> nc : listNode.getItemCommentPairs()) {
                lf = writeComments(nc.getValue(), indent, lf);
                if (lf || (prevnode != null && !(prevnode instanceof ListNode))) {
                    writer.append("\n").append(indent);
                }
                writer.append("- ");
                lf = internalWrite(nc.getKey(), listNode);
            }
            return lf;
        }
        return false;
    }

    private boolean writeComments(List<String> comments, String indent, boolean lf) throws IOException {
        if (comments == null) {
            return false;
        }
        for (String comment : comments) {
            if (lf) {
                writer.append('\n');
            }
            writer.append(indent).append('#').append(comment);
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


    private static Object internalMap(Node<?> curnode) {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            return valueNode.get();
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            TinyMap.Builder<String, Object> map = TinyMap.builder();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                map.put(e.getKey().toString(), internalMap(e.getValue()));
            }
            return map.build();
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            TinyList.Builder<Object> list = TinyList.builder();
            for (Node<?> node : listNode.get()) {
                list.add(internalMap(node));
            }
            return list.build();
        } else {
            return null;
        }
    }
}
