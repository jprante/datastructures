package org.xbib.datastructures.json.minimal;

import java.io.IOException;
import java.io.UncheckedIOException;

@SuppressWarnings("serial")
public class JsonException extends UncheckedIOException {

    public JsonException(String message) {
        super(new IOException(message));
    }

    public JsonException(IOException exception) {
        super(exception);
    }
}
