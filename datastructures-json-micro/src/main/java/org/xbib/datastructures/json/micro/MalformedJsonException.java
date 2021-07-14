package org.xbib.datastructures.json.micro;

public class MalformedJsonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MalformedJsonException(String msg) {
        super(msg);
    }
}
