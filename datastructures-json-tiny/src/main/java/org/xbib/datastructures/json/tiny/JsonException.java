package org.xbib.datastructures.json.tiny;

@SuppressWarnings("serial")
public class JsonException extends RuntimeException {

    public JsonException(String message) {
        super(message);
    }

    public JsonException(Exception exception) {
        super(exception);
    }
}
