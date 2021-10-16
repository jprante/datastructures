package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.ByteSizeValue;
import org.xbib.datastructures.api.DataStructure;
import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.api.Parser;
import org.xbib.datastructures.api.TimeValue;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;

public class Json implements DataStructure {

    private static final Json INSTANCE = new Json();

    private final char separator;

    private Node<?> root;

    public Json() {
        this(null);
    }

    public Json(Node<?> root) {
        this(root, '.');
    }

    public Json(Node<?> root, char separator) {
        this.root = root;
        this.separator = separator;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String yaml) throws IOException {
        return (Map<String, Object>) INSTANCE.createParser().parse(new StringReader(yaml)).get();
    }

    public static String toString(Map<String, Object> map) throws IOException {
        return INSTANCE.createBuilder().buildMap(map).build();
    }

    @Override
    public Parser createParser() {
        return new StreamParser();
    }

    @Override
    public Generator createGenerator(Node<?> root) {
        return new JsonGenerator(root);
    }

    @Override
    public Builder createBuilder() {
        return new JsonBuilder(new StringWriter());
    }

    @Override
    public Builder createBuilder(Consumer<String> consumer) {
        return new JsonBuilder(new StringWriter()) {
            @Override
            public String toString() {
                String string = super.toString();
                consumer.accept(string);
                return string;
            }
        };
    }

    @Override
    public void setRoot(Node<?> root) {
        this.root = root;
    }

    @Override
    public Node<?> getRoot() {
        return root;
    }

    @Override
    public Node<?> getNode(String path) {
        return path == null ? null : internalGetNode(root, path);
    }

    @Override
    public Boolean getBoolean(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Boolean.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Byte getByte(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Byte.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Short getShort(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Short.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Integer getInteger(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Long getLong(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Float getFloat(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Float.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Double getDouble(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public Character getCharacter(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        try {
            return value.charAt(0);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public String getString(String path) {
        return internalGet(path);
    }

    @Override
    public Instant getInstant(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        return Instant.parse(value);
    }

    @Override
    public TimeValue getAsTime(String path, TimeValue defaultValue) {
        return TimeValue.parseTimeValue(getString(path), defaultValue);
    }

    @Override
    public ByteSizeValue getAsBytesSize(String path, ByteSizeValue defaultValue) {
        return ByteSizeValue.parseBytesSizeValue(getString(path), defaultValue);
    }

    @Override
    public boolean set(String path, Object value) {
        return set(path, value, true);
    }

    private boolean set(String path, Object value, boolean rebuild) {
        if (rebuild) {
            if (value == null) {
                throw new IllegalStateException("value is null");
            }
            makeNode(path);
        }
        Node<?> node = getNode(path);
        if (!(node instanceof ValueNode)) {
            return false;
        }
        ValueNode valueNode = (ValueNode) node;
        if (value instanceof String
                || value instanceof Integer
                || value instanceof Double
                || value instanceof Long
                || value instanceof Boolean
                || value instanceof Float
                || value instanceof Short
                || value instanceof Character
                || value instanceof Byte) {
            valueNode.set(String.valueOf(value));
        } else if (value instanceof Instant) {
            Instant instant = (Instant) value;
            valueNode.set(instant.toString());
        } else {
            valueNode.set(value == null ? null : value.toString());
        }
        return true;
    }

    public boolean set(String path, Instant instant) {
        return set(path, instant.toString());
    }

    private Node<?> internalGetNode(Node<?> node, String path) {
        if (node == null || path.isEmpty()) {
            return node;
        }
        String[] sr = cut(path, separator);
        String sectName = sr[0];
        String restPath = sr[1];
        if (node instanceof MapNode) {
            return internalGetNode((Node<?>) ((MapNode) node).get().get(sectName), restPath);
        } else if (node instanceof ListNode) {
            try {
                return internalGetNode((Node<?>) ((ListNode) node).get().get(Integer.parseInt(sectName)), restPath);
            } catch (NumberFormatException ignore) {
                //
            }
        }
        return null;
    }

    private void makeNode(String path) {
        if (path != null) {
            internalMakeNode(root, path);
        }
    }

    private Node<?> internalMakeNode(Node<?> node, String path) {
        if (root != null && node == null) {
            return null;
        }
        String sectName;
        String nextSectName;
        String restPath;
        String[] sr = cut(path, separator);
        sectName = sr[0];
        restPath = sr[1];
        sr = cut(restPath, separator);
        nextSectName = sr[0];
        if (root == null) {
            if (path.isEmpty()) {
                root = new ValueNode("");
            } else {
                try {
                    int i = Integer.parseInt(sectName);
                    return root = internalMakeNode(root = new ListNode(), path);
                } catch (NumberFormatException ignore) {
                    return root = internalMakeNode(root = new MapNode(), path);
                }
            }
        }
        if (node instanceof MapNode) {
            MapNode hsnode = (MapNode) node;
            if (restPath.isEmpty()) {
                if (!hsnode.has(sectName)) {
                    hsnode.put(sectName, new ValueNode(""));
                }
            } else {
                try {
                    int i = Integer.parseInt(nextSectName);
                    if (!hsnode.has(sectName)) {
                        hsnode.put(sectName, internalMakeNode(new ListNode(), restPath));
                    } else {
                        internalMakeNode((Node<?>) hsnode.get().get(sectName), restPath);
                    }
                } catch (NumberFormatException ignore) {
                    if (!hsnode.has(sectName)) {
                        hsnode.put(sectName, internalMakeNode(new MapNode(), restPath));
                    } else {
                        internalMakeNode((Node<?>) hsnode.get().get(sectName), restPath);
                    }
                }
            }
        } else if (node instanceof ListNode) {
            ListNode lsnode = (ListNode) node;
            try {
                int i = Integer.parseInt(sectName);
                if (restPath.isEmpty()) {
                    if (!lsnode.has(i)) {
                        lsnode.add(i, new ValueNode(""));
                    }
                } else {
                    try {
                        i = Integer.parseInt(nextSectName);
                        if (!lsnode.has(i)) {
                            lsnode.add(i, internalMakeNode(new ListNode(), restPath));
                        } else {
                            internalMakeNode((Node<?>) lsnode.get().get(i), restPath);
                        }
                    } catch (NumberFormatException ignore) {
                        if (!lsnode.has(i)) {
                            lsnode.add(i, internalMakeNode(new MapNode(), restPath));
                        } else {
                            internalMakeNode((Node<?>) lsnode.get().get(i), restPath);
                        }
                    }
                }
            } catch (NumberFormatException ignore) {
                //
            }
        }
        return node;
    }

    private String internalGet(String path) {
        Node<?> node = getNode(unquote(path));
        return node instanceof ValueNode ? node.toString() : null;
    }

    private static String[] cut(String string, char delimiter) {
        int idx = string.indexOf(delimiter);
        String seg = idx == -1 ? string : string.substring(0, idx);
        String rest = idx == -1 ? "" : string.substring(idx + 1);
        return new String[]{seg, rest};
    }

    private static String unquote(String string) {
        if (string.startsWith("'") && string.endsWith("'")
                || string.startsWith("\"") && string.endsWith("\"")) {
            return string.substring(1, string.length() - 1);
        }
        return string;
    }
}
