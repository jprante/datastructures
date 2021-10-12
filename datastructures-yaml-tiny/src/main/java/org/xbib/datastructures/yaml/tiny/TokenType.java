package org.xbib.datastructures.yaml.tiny;

public enum TokenType {
    DOCUMENT_START,
    DOCUMENT_END,
    ITEM,
    KEY,
    VALUE,
    COMMENT,
    VALUE_LINE,
    VALUE_MULTILINE,
    VALUE_TEXT_PIPE,
    VALUE_TEXT_ANGLE,
    PIPE,
    ANGLE
}
