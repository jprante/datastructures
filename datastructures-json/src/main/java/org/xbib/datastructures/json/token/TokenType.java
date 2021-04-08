package org.xbib.datastructures.json.token;

public enum TokenType {
    NONE,
    BEGIN_ARRAY,
    END_ARRAY,
    BEGIN_OBJECT,
    END_OBJECT,
    NAME,
    VALUE,
    NULL,
    END_DOCUMENT
}
