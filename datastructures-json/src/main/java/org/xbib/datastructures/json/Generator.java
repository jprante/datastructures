package org.xbib.datastructures.json;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Generator {

    private final Writer writer;

    private JsonContext context;

    public Generator(Writer writer) {
        this.writer = writer;
    }

    public Generator beginArray() throws IOException {
        if (context != null) {
            beginStructure();
        }
        context = new JsonContext(context, JsonContext.StartArray);
        writer.write('[');
        return this;
    }

    public Generator endArray() throws IOException {
        writer.write(']');
        endStructure();
        return this;
    }

    public Generator beginObject() throws IOException {
        if (context != null) {
            beginStructure();
        }
        context = new JsonContext(context, JsonContext.StartObject);
        writer.write('{');
        return this;
    }

    public Generator endObject() throws IOException {
        writer.write('}');
        endStructure();
        return this;
    }

    public Generator writeString(String string) throws IOException {
        writeString(string, true);
        return this;
    }

    public Generator writeNull() throws IOException {
        writeString("null", false);
        return this;
    }

    public Generator writeObject(Object object) throws IOException {
        if (object == null) {
            writeNull();
            return this;
        }
        if (object instanceof String) {
            writeString((String) object);
            return this;
        }
        if (object instanceof Boolean) {
            writeBoolean((Boolean) object);
            return this;
        }
        if (object instanceof Byte) {
            writeNumber((byte) object);
            return this;
        }
        if (object instanceof Integer) {
            writeNumber((int) object);
            return this;
        }
        if (object instanceof Long) {
            writeNumber((long) object);
            return this;
        }
        if (object instanceof Float) {
            writeNumber((float) object);
            return this;
        }
        if (object instanceof Double) {
            writeNumber((double) object);
            return this;
        }
        if (object instanceof Number) {
            writeNumber((Number) object);
            return this;
        }
        throw new IllegalArgumentException("unable to write object class " + object.getClass());
    }

    public Generator writeBoolean(boolean bool) throws IOException {
        writeString(bool ? "true" : "false", false);
        return this;
    }

    public Generator writeNumber(byte number) throws IOException {
        writeString(Byte.toString(number), false);
        return this;
    }

    public Generator writeNumber(int number) throws IOException {
        writeString(Integer.toString(number), false);
        return this;
    }

    public Generator writeNumber(long number) throws IOException {
        writeString(Long.toString(number), false);
        return this;
    }

    public Generator writeNumber(float number) throws IOException {
        writeString(Float.toString(number), false);
        return this;
    }

    public Generator writeNumber(double number) throws IOException {
        writeString(Double.toString(number), false);
        return this;
    }

    public Generator writeNumber(Number number) throws IOException {
        writeString(number != null ? number.toString() : null);
        return this;
    }

    public Generator write(Map<String, Object> map) throws IOException {
        Objects.requireNonNull(map);
        beginObject();
        map.forEach((k, v) -> {
            try {
                writeString(k);
                writeObject(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        endObject();
        return this;
    }

    public Generator write(Collection<Object> collection) throws IOException {
        Objects.requireNonNull(collection);
        beginArray();
        collection.forEach(v -> {
            try {
                writeObject(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        endArray();
        return this;
    }

    private void writeString(String string, boolean escape) throws IOException {
        beforeWrite();
        if (escape) {
            if (string == null) {
                writeNull();
            } else {
                string(string);
            }
        } else {
            writer.write(string);
        }
        afterWrite();
    }

    private void beginStructure() throws IOException {
        final int state = context.state;
        switch (context.state) {
            case JsonContext.PropertyKey:
                writer.write(':');
                break;
            case JsonContext.ArrayValue:
                writer.write(',');
                break;
            case JsonContext.StartObject:
                break;
            case JsonContext.StartArray:
                break;
            default:
                throw new JsonException("illegal state : " + state);
        }
    }

    private void endStructure() {
        context = context.parent;
        if (context == null) {
            return;
        }
        int newState = -1;
        switch (context.state) {
            case JsonContext.PropertyKey:
                newState = JsonContext.PropertyValue;
                break;
            case JsonContext.StartArray:
                newState = JsonContext.ArrayValue;
                break;
            case JsonContext.ArrayValue:
                break;
            case JsonContext.StartObject:
                newState = JsonContext.PropertyKey;
                break;
            default:
                break;
        }
        if (newState != -1) {
            context.state = newState;
        }
    }

    private void beforeWrite() throws IOException {
        if (context == null) {
            return;
        }
        switch (context.state) {
            case JsonContext.StartObject:
            case JsonContext.StartArray:
                break;
            case JsonContext.PropertyKey:
                writer.write(':');
                break;
            case JsonContext.PropertyValue:
                writer.write(',');
                break;
            case JsonContext.ArrayValue:
                writer.write(',');
                break;
            default:
                break;
        }
    }

    private void afterWrite() {
        if (context == null) {
            return;
        }
        int newState = -1;
        switch (context.state) {
            case JsonContext.PropertyKey:
                newState = JsonContext.PropertyValue;
                break;
            case JsonContext.StartObject:
            case JsonContext.PropertyValue:
                newState = JsonContext.PropertyKey;
                break;
            case JsonContext.StartArray:
                newState = JsonContext.ArrayValue;
                break;
            case JsonContext.ArrayValue:
                break;
            default:
                break;
        }
        if (newState != -1) {
            context.state = newState;
        }
    }

    private void string(String string) throws IOException {
        writer.write('"');
        int start = 0;
        int l = string.length();
        for (int i = 0; i < l; i++) {
            char c = string.charAt(i);
            if (c == '"' || c < 32 || c >= 127 || c == '\\') {
                if (start < i) {
                    writer.write(string, start, i - start);
                }
                start = i;
                writer.write(escapeCharacter(c));
            }
        }
        if (start < l) {
            writer.write(string, start, l - start);
        }
        writer.write('"');
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

    private static class JsonContext {

        final static int StartObject = 1001;
        final static int PropertyKey = 1002;
        final static int PropertyValue = 1003;
        final static int StartArray = 1004;
        final static int ArrayValue = 1005;

        private final JsonContext parent;

        private int state;

        public JsonContext(JsonContext parent, int state) {
            this.parent = parent;
            this.state = state;
        }
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
