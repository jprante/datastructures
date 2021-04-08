package org.xbib.datastructures.csv;

public final class Token {

    TokenType type = TokenType.INVALID;

    StringBuilder content = new StringBuilder();

    boolean isReady;

    public void reset() {
        content.setLength(0);
        type = TokenType.INVALID;
        isReady = false;
    }

}
