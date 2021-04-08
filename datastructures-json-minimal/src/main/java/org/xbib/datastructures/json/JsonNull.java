package org.xbib.datastructures.json;

import java.io.IOException;

public class JsonNull extends JsonValue {
    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.writeString("null");
    }
}

