package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.ByteSizeValue;
import org.xbib.datastructures.api.TimeValue;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class YamlBuilder implements Builder {

    public final Appendable appendable;

    private final int indent;

    private State state;

    public YamlBuilder() {
        this(new StringBuilder());
    }

    public YamlBuilder(Appendable appendable) {
        this(appendable, 2);
    }

    public YamlBuilder(Appendable appendable, int indent) {
        this.appendable = appendable;
        this.indent = indent;
        this.state = new State(null, 0, Structure.MAP, false);
    }

    public static YamlBuilder builder() {
        return new YamlBuilder();
    }

    public static YamlBuilder builder(Appendable appendable) {
        return new YamlBuilder(appendable);
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
    public Builder buildCollection(Collection<?> collection) {
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
    public Builder copy(Builder builder) throws IOException {
        // TODO: no correct indent yet for copied yaml
        buildValue(builder.build());
        return this;
    }

    @Override
    public Builder copy(String string) throws IOException {
        buildValue(string);
        return this;
    }

    @Override
    public String build() {
        return appendable.toString();
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
        CharSequence charSequence = escape ? escapeString(string) : string;
        String value = charSequence.toString();
        if (!((value.startsWith("'") && value.endsWith("'")) || (value.startsWith("\"") && value.endsWith("\""))) &&
                value.matches(".*[?\\-#:>|$%&{}\\[\\]]+.*|[ ]+")) {
            if (value.contains("\"")) {
                value = "'" + value + "'";
            } else {
                value = "\"" + value + "\"";
            }
        }
        appendable.append(value);
    }

    private void beginKey(String k) throws IOException {
        if (state.parent != null && state.parent.item) {
            state.parent.item = false;
            return;
        }
        appendable.append(" ".repeat((state.level - 1) * indent));
    }

    private void endKey(String k) throws IOException {
        appendable.append(": ");
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
        appendable.append(" ".repeat((state.level - 1) * indent));
        appendable.append("- ");
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
        appendable.append(System.lineSeparator());
    }

    private CharSequence escapeString(CharSequence string) {
        StringBuilder sb = new StringBuilder();
        int start = 0;
        int l = string.length();
        for (int i = 0; i < l; i++) {
            char c = string.charAt(i);
            if (c == '"' || c < 32 || c >= 127 || c == '\\') {
                if (start < i) {
                    sb.append(string, start, i - start);
                }
                start = i;
                sb.append(escapeCharacter(c));
            }
        }
        if (start < l) {
            sb.append(string, start, l - start);
        }
        return sb;
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
