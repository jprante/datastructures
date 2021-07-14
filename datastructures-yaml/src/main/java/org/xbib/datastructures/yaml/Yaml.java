package org.xbib.datastructures.yaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.Instant;

public class Yaml {

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

    public void read(Reader reader) throws IOException {
        Parser parser = new Parser(reader);
        parser.parse();
        setRoot(parser.getNode());
    }

    public void save(Writer writer) throws IOException {
        new Generator(getRoot()).generate(writer);
    }

    public void setRoot(Node<?> root) {
        this.root = root;
    }

    public Node<?> getRoot() {
        return root;
    }

    public boolean exist(String path) {
        return getNode(path) != null;
    }

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

    public Boolean getBoolean(String path, Boolean defaultval) {
        return getBoolean(path) != null ? getBoolean(path) : defaultval;
    }

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

    public Byte getByte(String path, Byte defaultval) {
        return getByte(path) != null ? getByte(path) : defaultval;
    }

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

    public Short getShort(String path, Short defaultval) {
        return getShort(path) != null ? getShort(path) : defaultval;
    }

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

    public Integer getInteger(String path, Integer defaultval) {
        return getInteger(path) != null ? getInteger(path) : defaultval;
    }

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

    public Long getLong(String path, Long defaultval) {
        return getLong(path) != null ? getLong(path) : defaultval;
    }

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

    public Float getFloat(String path, Float defaultval) {
        return getFloat(path) != null ? getFloat(path) : defaultval;
    }

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

    public Double getDouble(String path, Double defaultval) {
        return getDouble(path) != null ? getDouble(path) : defaultval;
    }

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

    public Character getCharacter(String path, Character defaultval) {
        return getCharacter(path) != null ? getCharacter(path) : defaultval;
    }

    public String getString(String path) {
        return internalGet(path);
    }

    public String getString(String path, String defaultval) {
        return getString(path) != null ? getString(path) : defaultval;
    }

    public Instant getInstant(String path) {
        String value = internalGet(path);
        if (value == null) {
            return null;
        }
        return Instant.parse(value);
    }

    public Instant getInstant(String path, Instant defaultval) {
        Instant instant = getInstant(path);
        return instant != null ? instant : defaultval;
    }

    public boolean set(String path, Object value) {
        return set(path, value, true);
    }

    private boolean set(String path, Object value, boolean rebuild) {
        if (rebuild) {
            if (value == null) {
                //return removeNode(path);
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

    public boolean set(String path, Instant instant) {
        return set(path, instant.toString());
    }

    public Node<?> getNode(String path) {
        return path == null ? null : internalGetNode(root, path);
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

    public void makeNode(String path) {
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
        Node<?> node = getNode(dequote(path));
        return node instanceof ValueNode ? ((ValueNode) node).get() : null;
    }

    private static String[] cut(String string, char delimiter) {
        int idx = string.indexOf(delimiter);
        String seg = idx == -1 ? string : string.substring(0, idx);
        String rest = idx == -1 ? "" : string.substring(idx + 1);
        return new String[]{seg, rest};
    }

    private static String dequote(String string) {
        if (string.startsWith("'") && string.endsWith("'")
                || string.startsWith("\"") && string.endsWith("\"")) {
            return string.substring(1, string.length() - 1);
        }
        return string;
    }
}
