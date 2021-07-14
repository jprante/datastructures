package org.xbib.datastructures.json.minimal;

import java.io.IOException;

/**
 * A JSON literal.
 */
public class JsonLiteral extends JsonValue {

    public static final JsonLiteral NULL = new JsonLiteral("null");

    public static final JsonLiteral TRUE = new JsonLiteral("true");

    public static final JsonLiteral FALSE = new JsonLiteral("false");

    private final String value;

    private final boolean isNull;

    private final boolean isTrue;

    private final boolean isFalse;

    public JsonLiteral(String value) {
        this.value = value;
        isNull = "null".equals(value);
        isTrue = "true".equals(value);
        isFalse = "false".equals(value);
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.writeLiteral(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isNull() {
        return isNull;
    }

    @Override
    public boolean isTrue() {
        return isTrue;
    }

    @Override
    public boolean isFalse() {
        return isFalse;
    }

    @Override
    public boolean isBoolean() {
        return isTrue || isFalse;
    }

    @Override
    public boolean asBoolean() {
        return isNull ? super.asBoolean() : isTrue;
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object != null && getClass() == object.getClass() && value.equals(((JsonLiteral) object).value);
    }
}
