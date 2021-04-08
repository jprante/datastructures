package org.xbib.datastructures.json;

import java.io.IOException;

public class JsonBool extends JsonValue {

    private final boolean value;

    public JsonBool(boolean value) {
        this.value = value;
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.writeString(value ? "true" : "false");
    }
}
