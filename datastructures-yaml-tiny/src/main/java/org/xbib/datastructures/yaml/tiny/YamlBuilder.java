package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.ByteSizeValue;
import org.xbib.datastructures.api.TimeValue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class YamlBuilder implements Builder {

    public final Writer writer;

    private final int indent;

    private State state;

    public YamlBuilder(Writer writer) {
        this(writer, 2);
    }

    public YamlBuilder(Writer writer, int indent) {
        this.writer = writer;
        this.indent = indent;
        this.state = new State(null, 0, Structure.MAP, false);
    }

    @Override
    public Builder beginCollection() {
        this.state = new State(state, state.level + 1, Structure.COLLECTION, true);
        return this;
    }

    @Override
    public Builder endCollection() {
        if (state.structure != Structure.COLLECTION) {
            throw new YamlException("no array to close");
        }
        this.state = state.parent;
        return this;
    }

    @Override
    public Builder beginMap() {
        this.state = new State(state, state.level + 1, Structure.MAP, false);
        return this;
    }

    @Override
    public Builder endMap() {
        if (state.structure != Structure.MAP) {
            throw new YamlException("no object to close");
        }
        this.state = state.parent;
        return this;
    }

    @Override
    public Builder buildMap(Map<String, Object> map) {
        Objects.requireNonNull(map);
        beginMap();
        map.forEach((k, v) -> {
            try {
                buildKey(k);
                buildValue(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        endMap();
        return this;
    }

    @Override
    public Builder buildCollection(Collection<Object> collection) {
        Objects.requireNonNull(collection);
        beginCollection();
        collection.forEach(v -> {
            try {
                buildValue(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        endCollection();
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder buildValue(Object object) throws IOException {
        if (state.structure == Structure.MAP) {
            beginValue(object);
        }
        if (state.structure == Structure.COLLECTION) {
            beginArrayValue(object);
        }
        if (object == null) {
            buildNull();
        } else if (object instanceof Map) {
            buildMap((Map<String, Object>) object);
        } else if (object instanceof Collection) {
            buildCollection((Collection<Object>) object);
        } else if (object instanceof CharSequence) {
            buildString((CharSequence) object, true);
        } else if (object instanceof Boolean) {
            buildBoolean((Boolean) object);
        } else if (object instanceof Byte) {
            buildNumber((byte) object);
        } else if (object instanceof Integer) {
            buildNumber((int) object);
        } else if (object instanceof Long) {
            buildNumber((long) object);
        } else if (object instanceof Float) {
            buildNumber((float) object);
        } else if (object instanceof Double) {
            buildNumber((double) object);
        } else if (object instanceof Number) {
            buildNumber((Number) object);
        } else if (object instanceof Instant) {
            buildInstant((Instant) object);
        } else if (object instanceof ByteSizeValue) {
            buildKey(object.toString());
        } else if (object instanceof TimeValue) {
            buildKey(object.toString());
        } else {
            throw new IllegalArgumentException("unable to write object class " + object.getClass());
        }
        if (state.structure == Structure.MAP) {
            endValue(object);
        }
        if (state.structure == Structure.COLLECTION) {
            endArrayValue(object);
        }
        return this;
    }

    @Override
    public Builder buildKey(CharSequence charSequence) throws IOException {
        if (state.structure == Structure.MAP) {
            beginKey(charSequence.toString());
        }
        buildString(charSequence, true);
        if (state.structure == Structure.MAP) {
            endKey(charSequence.toString());
        }
        return this;
    }

    @Override
    public Builder buildNull() throws IOException {
        buildString("null", false);
        return this;
    }

    @Override
    public String build() {
        return writer.toString();
    }

    private void buildNumber(Number number) throws IOException {
        buildString(number != null ? number.toString() : null, false);
    }

    private void buildBoolean(boolean bool) throws IOException {
        buildString(bool ? "true" : "false", true);
    }

    private void buildInstant(Instant instant) throws IOException{
        buildString(instant.toString(), false);
    }

    private void buildString(CharSequence string, boolean escape) throws IOException {
        String value = escape ? escapeString(string) : string.toString();
        if (!((value.startsWith("'") && value.endsWith("'")) || (value.startsWith("\"") && value.endsWith("\""))) &&
                value.matches(".*[?\\-#:>|$%&{}\\[\\]]+.*|[ ]+")) {
            if (value.contains("\"")) {
                value = "'" + value + "'";
            } else {
                value = "\"" + value + "\"";
            }
        }
        writer.write(value);
    }

    private void beginKey(String k) throws IOException {
        if (state.parent != null && state.parent.item) {
            state.parent.item = false;
            return;
        }
        writer.write(" ".repeat((state.level - 1) * indent));
    }

    private void endKey(String k) throws IOException {
        writer.write(": ");
    }

    private void beginValue(Object v) throws IOException {
        if (v instanceof Map) {
            writeLn();
            return;
        }
        if (v instanceof Collection) {
            writeLn();
        }
    }

    private void endValue(Object v) throws IOException{
        if (v instanceof Map) {
            return;
        }
        if (v instanceof Collection) {
            return;
        }
        writeLn();
    }

    private void beginArrayValue(Object v) throws IOException {
        if (v instanceof Collection) {
            return;
        }
        writer.write(" ".repeat((state.level - 1) * indent));
        writer.write("- ");
        state.item = true;
    }

    private void endArrayValue(Object v) throws IOException {
        if (v instanceof Map) {
            return;
        }
        if (v instanceof Collection) {
            return;
        }
        writeLn();
    }

    private void writeLn() throws IOException{
        writer.write(System.lineSeparator());
    }

    private String escapeString(CharSequence string) {
        StringBuilder sb = new StringBuilder();
        int start = 0;
        int l = string.length();
        for (int i = 0; i < l; i++) {
            char c = string.charAt(i);
            if (c == '"' || c < 32 || c >= 127 || c == '\\') {
                if (start < i) {
                    sb.append(string.toString(), start, i - start);
                }
                start = i;
                sb.append(escapeCharacter(c));
            }
        }
        if (start < l) {
            sb.append(string.toString(), start, l - start);
        }
        return sb.toString();
    }

    private static String escapeCharacter(char c) {
        switch (c) {
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\t':
                return "\\t";
            case '\\':
                return "\\\\";
            case '\'':
                return "\\'";
            case '\"':
                return "\\\"";
        }
        String hex = Integer.toHexString(c);
        return "\\u0000".substring(0, 6 - hex.length()) + hex;
    }

    private enum Structure { MAP, COLLECTION };

    private static class State {
        State parent;
        int level;
        Structure structure;
        boolean item;

        State(State parent, int level, Structure structure, boolean item) {
            this.parent = parent;
            this.level = level;
            this.structure = structure;
            this.item = item;
        }
    }
}
