package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.ByteSizeValue;
import org.xbib.datastructures.api.TimeValue;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class JsonBuilder implements Builder {

    private final Appendable appendable;

    private State state;

    public JsonBuilder() {
        this(new StringBuilder());
    }

    public JsonBuilder(Appendable appendable) {
        this.appendable = appendable;
        this.state = new State(null, 0, Structure.DOCSTART, true);
    }

    public static JsonBuilder builder() {
        return new JsonBuilder();
    }

    public static JsonBuilder builder(Appendable appendable) {
        return new JsonBuilder(appendable);
    }

    @Override
    public Builder beginCollection() throws IOException {
        this.state = new State(state, state.level + 1, Structure.COLLECTION, true);
        appendable.append('[');
        return this;
    }

    @Override
    public Builder endCollection() throws IOException {
        if (state.structure != Structure.COLLECTION) {
            throw new JsonException("no array to close");
        }
        appendable.append(']');
        this.state = state != null ? state.parent : null;
        return this;
    }

    @Override
    public Builder beginMap() throws IOException {
        this.state = new State(state, state.level + 1, Structure.MAP, true);
        appendable.append('{');
        return this;
    }

    @Override
    public Builder endMap() throws IOException {
        if (state.structure != Structure.MAP && state.structure != Structure.KEY) {
            throw new JsonException("no object to close");
        }
        appendable.append('}');
        this.state = state != null ? state.parent : null;
        return this;
    }

    @Override
    public Builder buildMap(Map<String, Object> map) throws IOException {
        Objects.requireNonNull(map);
        if (state.structure == Structure.COLLECTION) {
            beginArrayValue(map);
        }
        boolean wrap = state.structure != Structure.MAP;
        if (wrap) {
            beginMap();
        }
        map.forEach((k, v) -> {
            try {
                buildKey(k);
                buildValue(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        if (wrap) {
            endMap();
        }
        if (state.structure == Structure.COLLECTION) {
            endArrayValue(map);
        }
        return this;
    }

    @Override
    public Builder buildCollection(Collection<?> collection) throws IOException {
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
        if (object instanceof Map) {
            buildMap((Map<String, Object>) object);
            return this;
        } else if (object instanceof Collection) {
            buildCollection((Collection<Object>) object);
            return this;
        }
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            beginValue(object);
        } else if (state.structure == Structure.COLLECTION) {
            beginArrayValue(object);
        }
        if (object == null) {
            buildNull();
        } else if (object instanceof CharSequence) {
            buildString((CharSequence) object, true);
        } else if (object instanceof Boolean) {
            buildBoolean((Boolean) object);
        } else  if (object instanceof Byte) {
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
            buildString(object.toString(), false);
        } else if (object instanceof TimeValue) {
            buildString(object.toString(), false);
        } else {
            throw new IllegalArgumentException("unable to write object class " + object.getClass());
        }
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            endValue(object);
        } else if (state.structure == Structure.COLLECTION) {
            endArrayValue(object);
        }
        return this;
    }

    @Override
    public Builder buildKey(CharSequence string) throws IOException {
        if (state.structure == Structure.COLLECTION) {
            beginArrayValue(string);
        } else if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            beginKey(string != null ? string.toString() : null);
        }
        buildString(string, true);
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            endKey(string != null ? string.toString() : null);
        }
        state.structure = Structure.KEY;
        return this;
    }

    @Override
    public Builder buildNull() throws IOException {
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            beginValue(null);
        } else if (state.structure == Structure.COLLECTION) {
            beginArrayValue(null);
        }
        buildString("null", false);
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            endValue(null);
        } else if (state.structure == Structure.COLLECTION) {
            endArrayValue(null);
        }
        return this;
    }

    @Override
    public Builder copy(Builder builder) throws IOException {
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            beginValue(null);
        } else if (state.structure == Structure.COLLECTION) {
            beginArrayValue(null);
        }
        appendable.append(builder.build());
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            endValue(null);
        }
        if (state.structure == Structure.COLLECTION) {
            endArrayValue(null);
        }
        return this;
    }

    @Override
    public String build() {
        return appendable.toString();
    }

    private void beginKey(String k) throws IOException {
        if (state.first) {
            state.first = false;
        } else {
            appendable.append(",");
        }
    }

    private void endKey(String k) throws IOException {
        appendable.append(":");
    }

    private void beginValue(Object v) {
    }

    private void endValue(Object v) {
    }

    private void beginArrayValue(Object v) throws IOException {
        if (state.first) {
            state.first = false;
        } else {
            appendable.append(",");
        }
    }

    private void endArrayValue(Object v) {
    }

    private void buildBoolean(boolean bool) throws IOException {
        buildString(bool ? "true" : "false", false);
    }

    private void buildNumber(Number number) throws IOException {
        buildString(number != null ? number.toString() : null, false);
    }

    private void buildInstant(Instant instant) throws IOException{
        buildString(instant.toString(), true);
    }

    private void buildString(CharSequence string, boolean escape) throws IOException {
        appendable.append(escape ? escapeString(string) : string);
    }

    private CharSequence escapeString(CharSequence string) {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        int start = 0;
        int l = string.length();
        for (int i = 0; i < l; i++) {
            char c = string.charAt(i);
            if (c == '"' || c == '\\' || c < 32) {
                if (i > start) {
                    sb.append(string, start, i);
                }
                start = i + 1;
                sb.append(escapeCharacter(c));
            }
        }
        if (l > start) {
            sb.append(string, start, l);
        }
        sb.append('"');
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

    private enum Structure { DOCSTART, MAP, KEY, COLLECTION };

    private static class State {
        State parent;
        int level;
        Structure structure;
        boolean first;

        State(State parent, int level, Structure structure, boolean first) {
            this.parent = parent;
            this.level = level;
            this.structure = structure;
            this.first = first;
        }
    }
}
