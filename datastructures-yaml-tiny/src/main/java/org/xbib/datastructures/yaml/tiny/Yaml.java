package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.*;
import org.xbib.datastructures.api.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;

public class Yaml implements DataStructure {

    private static final Yaml INSTANCE = new Yaml();
;
    private final char separator;

    private Node<?> root;

    public Yaml() {
        this(null);
    }

    public Yaml(Node<?> root) {
        this(root, '.');
    }

    public Yaml(Node<?> root, char separator) {
        this.root = root;
        this.separator = separator;
    }

    public static Map<String, Object> toMap(String yaml) throws IOException {
        return toMap(new StringReader(yaml));
    }

    public static Map<String, Object> toMap(Reader reader) throws IOException {
        // buffered reader is required for mark() support
        try (BufferedReader bufferedReader = new BufferedReader(reader)){
            return YamlGenerator.toMap(INSTANCE.createParser().parse(bufferedReader));
        }
    }

    public static String toString(Map<String, Object> map) throws IOException {
        return INSTANCE.createBuilder().buildMap(map).build();
    }

    @Override
    public Parser createParser() {
        return new YamlParser();
    }

    @Override
    public Builder createBuilder() {
        return new YamlBuilder(new StringWriter());
    }

    @Override
    public Builder createBuilder(Consumer<String> consumer) {
        return new YamlBuilder(new StringWriter()) {
            @Override
            public String build() {
                String string = super.build();
                consumer.accept(string);
                return string;
            }
        };
    }

    @Override
    public Generator createGenerator(Node<?> root) {
        return new YamlGenerator(root);
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
        } catch (NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (NumberFormatException | NullPointerException ignore) {
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
        } catch (IndexOutOfBoundsException | NullPointerException ignore) {
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
        ValueNode txnode = (ValueNode) node;
        if (value instanceof String
                || value instanceof Integer
                || value instanceof Double
                || value instanceof Long
                || value instanceof Boolean
                || value instanceof Float
                || value instanceof Short
                || value instanceof Character
                || value instanceof Byte) {
            txnode.set(String.valueOf(value));
        } else if (value instanceof Instant) {
            Instant instant = (Instant) value;
            txnode.set(instant.toString());
        } else {
            txnode.set(value == null ? null : value.toString());
        }
        return true;
    }

    private Node<?> internalGetNode(Node<?> node, String path) {
        if (node == null || path.isEmpty()) {
            return node;
        }
        String[] sr = cut(path, separator);
        String sectName = sr[0];
        String restPath = sr[1];
        if (node instanceof MapNode) {
            return internalGetNode(((MapNode) node).get(sectName), restPath);
        } else if (node instanceof ListNode) {
            try {
                return internalGetNode(((ListNode) node).get(Integer.parseInt(sectName)), restPath);
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
                root = new ValueNode(node);
            } else {
                try {
                    int i = Integer.parseInt(sectName);
                    return root = internalMakeNode(root = new ListNode(node), path);
                } catch (NumberFormatException ignore) {
                    return root = internalMakeNode(root = new MapNode(node), path);
                }
            }
        }
        if (node instanceof MapNode) {
            MapNode hsnode = (MapNode) node;
            if (restPath.isEmpty()) {
                if (!hsnode.has(sectName)) {
                    hsnode.put(sectName, new ValueNode(hsnode));
                }
            } else {
                try {
                    int i = Integer.parseInt(nextSectName);
                    if (!hsnode.has(sectName)) {
                        hsnode.put(sectName, internalMakeNode(new ListNode(node), restPath));
                    } else {
                        internalMakeNode(hsnode.get(sectName), restPath);
                    }
                } catch (NumberFormatException ignore) {
                    if (!hsnode.has(sectName)) {
                        hsnode.put(sectName, internalMakeNode(new MapNode(node), restPath));
                    } else {
                        internalMakeNode(hsnode.get(sectName), restPath);
                    }
                }
            }
        } else if (node instanceof ListNode) {
            ListNode lsnode = (ListNode) node;
            try {
                int i = Integer.parseInt(sectName);
                if (restPath.isEmpty()) {
                    if (!lsnode.has(i)) {
                        lsnode.add(i, new ValueNode(lsnode));
                    }
                } else {
                    try {
                        i = Integer.parseInt(nextSectName);
                        if (!lsnode.has(i)) {
                            lsnode.add(i, internalMakeNode(new ListNode(node), restPath));
                        } else {
                            internalMakeNode(lsnode.get(i), restPath);
                        }
                    } catch (NumberFormatException ignore) {
                        if (!lsnode.has(i)) {
                            lsnode.add(i, internalMakeNode(new MapNode(node), restPath));
                        } else {
                            internalMakeNode(lsnode.get(i), restPath);
                        }
                    }
                }
            } catch (NumberFormatException ignore) {
            }
        }
        return node;
    }

    private String internalGet(String path) {
        Node<?> node = getNode(unquote(path));
        return node instanceof ValueNode ? ((ValueNode) node).get().toString() : null;
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
