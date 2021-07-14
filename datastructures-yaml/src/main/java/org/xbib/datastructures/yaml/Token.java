package org.xbib.datastructures.yaml;

public class Token {

    private final TokenType type;

    private final int depth;

    private final String value;

    public Token(TokenType type, int depth) {
        this(type, depth, null);
    }

    public Token(TokenType type, int depth, String value) {
        this.type = type;
        this.depth = depth;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public int getDepth() {
        return depth;
    }

    public String getValue() {
        return value;
    }
}
