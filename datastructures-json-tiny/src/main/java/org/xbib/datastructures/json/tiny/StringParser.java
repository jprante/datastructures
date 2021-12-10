package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

import java.util.Objects;

public class StringParser {

    private static final char EOS = (char) -1;

    private static final char DOUBLE_QUOTE = '"';

    private static final char BACKSLASH = '\\';

    private static final char OPEN_MAP = '{';

    private static final char CLOSE_MAP = '}';

    private static final char OPEN_LIST = '[';

    private static final char CLOSE_LIST = ']';

    private static final char COMMA = ',';

    private static final char COLON = ':';

    private final JsonResult listener;

    private String input;

    private int i;

    private char ch;

    public StringParser() {
        this(new TinyJsonListener());
    }

    public StringParser(JsonResult listener) {
        this.listener = listener;
    }

    public Node<?> parse(String input) throws JsonException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(listener);
        this.input = input;
        this.i = 0;
        listener.begin();
        ch = next();
        skipWhitespace();
        parseValue();
        skipWhitespace();
        if (ch != EOS) {
            throw new JsonException("malformed json: " + ch);
        }
        listener.end();
        return listener.getResult();
    }

    public Node<?> getNode() {
        return listener.getResult();
    }

    private void parseValue() throws JsonException {
        switch (ch) {
            case DOUBLE_QUOTE:
                ch = next();
                parseString(false);
                break;
            case OPEN_MAP:
                parseMap();
                break;
            case OPEN_LIST:
                parseList();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
                parseNumber();
                break;
            case 't':
                parseTrue();
                break;
            case 'f':
                parseFalse();
                break;
            case 'n':
                parseNull();
                break;
            default:
                throw new JsonException("illegal character: " + ch);
        }
    }

    private void parseNumber() throws JsonException {
        boolean minus = false;
        boolean dot = false;
        boolean exponent = false;
        int start = i - 1;
        while (true) {
            if (ch == '-') {
                if (i - start > 1) {
                    throw new JsonException("minus inside number");
                }
                ch =next();
                minus = true;
            } else if (ch == 'e' || ch == 'E') {
                ch = next();
                if (exponent) {
                    throw new JsonException("double exponents");
                }
                exponent = true;
                ch = next();
                if (ch == '-' || ch == '+') {
                    ch = next();
                    if (ch < '0' || ch > '9') {
                        throw new JsonException("invalid exponent");
                    }
                } else if (ch < '0' || ch > '9') {
                    throw new JsonException("invalid exponent");
                }
            } else if (ch == '.') {
                ch = next();
                if (dot) {
                    throw new JsonException("multiple dots");
                }
                if (i - start == 1) {
                    throw new JsonException("no digit before dot");
                }
                dot = true;
            } else if (ch >= '0' && ch <= '9') {
                ch = next();
            } else {
                break;
            }
        }
        if (minus && i - start == 1) {
            throw new JsonException("isolated minus");
        }
        if (dot || exponent) {
            listener.onDouble(FastDoubleParser.parseDouble(input.substring(start, i - 1)));
        } else {
            listener.onLong(Long.parseLong(input.substring(start, i - 1)));
        }
    }

    private void parseString(boolean isKey) throws JsonException {
        boolean escaped = false;
        int start = i - 1;
        while (true) {
            if (ch == DOUBLE_QUOTE) {
                if (escaped) {
                    CharSequence s = unescape(input.substring(start, i - 1));
                    if (isKey) {
                        listener.onKey(s);
                    } else {
                        listener.onValue(s);
                    }
                } else {
                    if (isKey) {
                        listener.onKey(input.substring(start, i - 1));
                    } else {
                        listener.onValue(input.substring(start, i - 1));
                    }
                }
                ch = next();
                return;
            } else if (ch == BACKSLASH) {
                escaped = true;
                ch = next();
                if (ch == DOUBLE_QUOTE || ch == '/' || ch == BACKSLASH || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't') {
                    ch = next();
                } else if (ch == 'u') {
                    expectHex();
                    expectHex();
                    expectHex();
                    expectHex();
                } else {
                    throw new JsonException("illegal escape char: " + ch);
                }
            } else if (ch < 32) {
                throw new JsonException("illegal control char: " + ch);
            } else {
                ch = next();
            }
        }
    }

    private void parseList() {
        int count = 0;
        listener.beginCollection();
        ch = next();
        while (true) {
            skipWhitespace();
            if (ch == CLOSE_LIST) {
                listener.endCollection();
                ch = next();
                return;
            }
            if (count > 0) {
                expectChar(COMMA);
                ch = next();
                skipWhitespace();
            }
            parseValue();
            count++;
        }
    }

    private void parseMap() {
        int count = 0;
        listener.beginMap();
        ch = next();
        while (true) {
            skipWhitespace();
            if (ch == CLOSE_MAP) {
                listener.endMap();
                ch = next();
                return;
            }
            if (count > 0) {
                expectChar(COMMA);
                ch = next();
                skipWhitespace();
            }
            expectChar(DOUBLE_QUOTE);
            ch = next();
            parseString(true);
            skipWhitespace();
            expectChar(COLON);
            ch = next();
            skipWhitespace();
            parseValue();
            count++;
        }
    }

    private void parseNull() throws JsonException {
        ch = next();
        expectChar('u');
        ch = next();
        expectChar('l');
        ch = next();
        expectChar('l');
        listener.onNull();
        ch = next();
    }

    private void parseTrue() throws JsonException {
        ch = next();
        expectChar('r');
        ch = next();
        expectChar('u');
        ch = next();
        expectChar('e');
        listener.onTrue();
        ch = next();
    }

    private void parseFalse() throws JsonException {
        ch = next();
        expectChar('a');
        ch = next();
        expectChar('l');
        ch = next();
        expectChar('s');
        ch = next();
        expectChar('e');
        listener.onFalse();
        ch = next();
    }

    private void expectChar(char expected) throws JsonException {
        if (ch != expected) {
            throw new JsonException("expected char " + expected + " but got " + ch);
        }
    }

    private void expectHex() throws JsonException {
        ch = next();
        if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
            return;
        }
        throw new JsonException("invalid hex char " + ch);
    }

    private void skipWhitespace() {
        while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            ch = next();
        }
    }

    private static CharSequence unescape(CharSequence input) {
        StringBuilder result = new StringBuilder(input.length());
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == BACKSLASH) {
                i++;
                switch (input.charAt(i)) {
                    case BACKSLASH:
                        result.append(BACKSLASH);
                        break;
                    case '/':
                        result.append('/');
                        break;
                    case DOUBLE_QUOTE:
                        result.append(DOUBLE_QUOTE);
                        break;
                    case 'b':
                        result.append('\b');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case 'u': {
                        result.append(Character.toChars(Integer.parseInt(input.toString().substring(i + 1, i + 5), 16)));
                        i += 4;
                    }
                }
            } else {
                result.append(input.charAt(i));
            }
            i++;
        }
        return result;
    }

    private char next() {
        try {
            return input.charAt(i++);
        } catch (StringIndexOutOfBoundsException e) {
            return (char) -1;
        }
    }
}
