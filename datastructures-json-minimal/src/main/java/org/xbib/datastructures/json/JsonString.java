package org.xbib.datastructures.json;

import java.io.IOException;
import java.util.Objects;

public class JsonString extends JsonValue {

    private final String string;

    public JsonString(String string) {
        Objects.requireNonNull(string);
        this.string = string;
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.writeString(string);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String asString() {
        return string;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object != null && getClass() == object.getClass()
                && string.equals(((JsonString) object).string);
    }
}
