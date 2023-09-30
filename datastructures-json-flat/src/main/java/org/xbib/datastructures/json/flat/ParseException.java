package org.xbib.datastructures.json.flat;

import java.io.IOException;

@SuppressWarnings("serial")
public class ParseException extends IOException {

    public ParseException(String message) {
        super(message);
    }

}
