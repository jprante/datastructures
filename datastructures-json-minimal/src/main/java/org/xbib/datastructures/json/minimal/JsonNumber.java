package org.xbib.datastructures.json.minimal;

import java.io.IOException;
import java.util.Objects;

public class JsonNumber extends JsonValue {

    private final String string;

    public JsonNumber(String string) {
        Objects.requireNonNull(string);
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.writeNumber(string);
    }

    @Override
    public boolean isInt() {
        try {
            asInt();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isLong() {
        try {
            asLong();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isFloat() {
        try {
            asFloat();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isDouble() {
        try {
            asDouble();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(string, 10);
    }

    @Override
    public long asLong() {
        return Long.parseLong(string, 10);
    }

    @Override
    public float asFloat() {
        return Float.parseFloat(string);
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object != null && getClass() == object.getClass()
                && string.equals(((JsonNumber) object).string);
    }
}
