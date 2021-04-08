package org.xbib.datastructures.yaml;

import org.xbib.datastructures.yaml.token.Token;
import org.xbib.datastructures.yaml.token.TokenType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lexer {

    private static final char EOF = (char) -1;

    private static final char EOL = (char) -2;

    private static final char BOL = (char) -3;

    private final BufferedReader bufferedReader;

    private final StringBuilder stringBuilder;

    private char[] line;

    private int lineno;

    private int index;

    private final Queue<Token> tokens;

    private Token prevToken;

    private Token nextToken;

    public Lexer(Reader reader) {
        this.bufferedReader = new BufferedReader(reader);
        this.stringBuilder = new StringBuilder();
        tokens = new LinkedList<>();
    }

    public Token next() throws IOException {
        if (!tokens.isEmpty()) {
            return tokens.poll();
        }
        Token token;
        if (nextToken != null) {
            token = nextToken;
            nextToken = null;
        } else {
            token = nextToken();
        }
        if (token == null) {
            return null;
        }
        boolean isPipe = false;
        switch (token.getType()) {
            case VALUE:
                List<String> values = new ArrayList<>();
                int indent = token.getDepth();
                do {
                    if (token.getType() == TokenType.COMMENT) {
                        tokens.add(token);
                    } else if (token.getType() == TokenType.VALUE) {
                        values.add(token.getValue());
                    } else {
                        break;
                    }
                } while ((token = nextToken()) != null && token.getDepth() > prevToken.getDepth());
                nextToken = token;
                tokens.add(values.size() == 1
                        ? new Token(TokenType.VALUE_LINE, indent, values.get(0))
                        : new Token(TokenType.VALUE_MULTILINE, indent, String.join(" ", values)));
                return next();
            case PIPE:
                isPipe = true;
            case ANGLE:
                List<Token> valueTokens = new ArrayList<>();
                token = nextToken();
                indent = token != null ? token.getDepth() : 0;
                if (indent > prevToken.getDepth()) {
                    do {
                        valueTokens.add(token);
                    }
                    while ((token = nextToken()) != null && token.getDepth() > prevToken.getDepth());
                    nextToken = token;
                    StringBuilder sb = new StringBuilder();
                    boolean lf = false;
                    for (Token valtok : valueTokens) {
                        if (lf) {
                            if (isPipe) {
                                sb.append('\n');
                            } else {
                                sb.append(' ');
                            }
                        }
                        sb.append(" ".repeat(valtok.getDepth() - indent));
                        if (valtok.getType() == TokenType.COMMENT) {
                            sb.append('#');
                        }
                        sb.append(valtok.getValue());
                        lf = true;
                    }
                    return new Token(isPipe ? TokenType.VALUE_TEXT_PIPE : TokenType.VALUE_TEXT_ANGLE, indent, sb.toString());
                } else {
                    new Token(TokenType.VALUE_LINE, indent);
                }
            default:
                if (token != null && (token.getType() == TokenType.KEY || token.getType() == TokenType.ITEM)) {
                    prevToken = token;
                }
                return token;
        }
    }

    private Token nextToken() throws IOException {
        if (isEOF() || isEOL()) {
            readline();
            if (isEOF()) {
                return null;
            }
        }
        int indent;
        String value;
        if (isBlank()) {
            skipBlanks();
        }
        if (isEOL()) {
            return nextToken();
        }
        if (isHash()) {
            indent = index;
            read();
            value = new String(line).substring(index);
            readline();
            return new Token(TokenType.COMMENT, indent, value);
        } else if (isHyphen() && hasRightGap()) {
            indent = index;
            read(2);
            return new Token(TokenType.ITEM, indent);
        } else if (isPipe()) {
            indent = index;
            read();
            return new Token(TokenType.PIPE, indent);
        } else if (isRAngle()) {
            indent = index;
            read();
            return new Token(TokenType.ANGLE, indent);
        } else if (isText()) {
            indent = index;
            value = extractText();
            if (isColon() && hasRightGap()) {
                read(2);
                return new Token(TokenType.KEY, indent, value);
            } else {
                return new Token(TokenType.VALUE, indent, value);
            }
        }
        throw new YamlException("Syntax error: (" + lineno + ":" + index + ":" + new String(line));
    }

    private String extractText() throws IOException {
        stringBuilder.setLength(0);
        if (isSQuote()) {
            read();
            while (!isSQuote() && !isEOL()) {
                stringBuilder.append(current());
                read();
            }
            read();
        } else if (isDQuote()) {
            read();
            while (!isDQuote() && !isEOL()) {
                if (isBSlash()) {
                    read();
                    switch (current()) {
                        case '0':
                            stringBuilder.append('\0');
                            break;
                        case 'n':
                            stringBuilder.append('\n');
                            break;
                        case 'r':
                            stringBuilder.append('\r');
                            break;
                        case 't':
                            stringBuilder.append('\t');
                            break;
                        case '\"':
                            stringBuilder.append('\"');
                            break;
                        case '\\':
                            stringBuilder.append('\\');
                            break;
                        default:
                            stringBuilder.append('\\');
                            stringBuilder.append(current());
                            break;
                    }
                } else {
                    stringBuilder.append(current());
                }
                read();
            }
            read();
        } else {
            while (!isEOF() && !isEOL() && !(isColon() && hasRightGap()) && !(isHash() && hasLeftGap())) {
                stringBuilder.append(current());
                read();
            }
        }
        return stringBuilder.toString().trim();
    }

    private void readline() throws IOException {
        String buf;
        do {
            buf = bufferedReader.readLine();
            lineno++;
        } while (buf != null && buf.trim().isEmpty());
        line = buf != null ? buf.toCharArray() : null;
        index = 0;
    }

    private void read() throws IOException {
        read(1);
    }

    private void read(int cnt) throws IOException {
        index += cnt;
        if (index > line.length) {
            readline();
            index = 0;
        }
    }

    private char ch(int index) {
        return line == null ? EOF
                : (index >= 0 && index < line.length) ? line[index]
                : index >= line.length ? EOL : BOL;
    }

    private char prevChar() {
        return ch(index - 1);
    }

    private char current() {
        return ch(index);
    }

    private char nextChar() {
        return ch(index + 1);
    }

    private boolean hasLeftGap() {
        return prevChar() == ' ' || prevChar() == BOL;
    }

    private boolean hasRightGap() {
        return nextChar() == ' ' || nextChar() == EOL;
    }

    private boolean isEOF() {
        return current() == EOF;
    }

    private boolean isEOL() {
        return current() == EOL;
    }

    private boolean isBSlash() {
        return current() == '\\';
    }

    private boolean isBlank() {
        return current() == ' ';
    }

    private boolean isHyphen() {
        return current() == '-';
    }

    private boolean isColon() {
        return current() == ':';
    }

    private boolean isRAngle() {
        return current() == '>';
    }

    private boolean isPipe() {
        return current() == '|';
    }

    private boolean isHash() {
        return current() == '#';
    }

    private boolean isSQuote() {
        return current() == '\'';
    }

    private boolean isDQuote() {
        return current() == '"';
    }

    private boolean isText() {
        return !(current() == EOF
                || current() == '#'
                || current() == '|'
                || (current() == '-' && hasRightGap()));
    }

    private void skipBlanks() throws IOException {
        while (isBlank()) {
            read();
        }
    }
}
