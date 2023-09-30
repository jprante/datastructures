package org.xbib.datastructures.json.flat;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final int TYPE = 0;

    private static final int FROM = 1;

    private static final int TO = 2;

    private static final int NESTED = 3;

    private final String input;

    private final List<int[]> blocks;

    private final int blockSize;

    private int element;

    public Parser(String input) throws ParseException {
        if (input == null) {
            throw new ParseException("cannot parse null");
        }
        this.input = input;
        this.blocks = new ArrayList<>();
        this.blockSize = 4 * Math.min(Math.max(input.length() / 16, 4), 1024);
        this.element = 0;
        parse();
    }

    Type getType(int element) {
        return Type.values()[getComponent(element, TYPE)];
    }

    int getNested(int element) {
        return getComponent(element, NESTED);
    }

    String getJson(int element) {
        return input.substring(getComponent(element, FROM), getComponent(element, TO) + 1);
    }

    String getUnescapedString(int element) {
        String value = input.substring(getComponent(element, FROM) + 1, getComponent(element, TO));
        return getType(element) == Type.STRING_ESCAPED ? StringUtil.unescape(value) : value;
    }

    void accept(int element, Visitor visitor) {
        Type type = getType(element);
        switch (type) {
            case NULL:
                visitor.onNull();
                break;
            case TRUE:
                visitor.onBoolean(true);
                break;
            case FALSE:
                visitor.onBoolean(false);
                break;
            case NUMBER:
                visitor.onNumber(getJson(element));
                break;
            case STRING_ESCAPED:
            case STRING:
                visitor.onString(getUnescapedString(element));
                break;
            case ARRAY:
                acceptArray(element, visitor);
                break;
            case OBJECT:
                acceptObject(element, visitor);
                break;
            default:
                throw new IllegalStateException("unknown type: " + type);
        }
    }

    private void acceptArray(int element, Visitor visitor) {
        visitor.beginArray();
        int e = element + 1;
        while (e <= element + getNested(element)) {
            accept(e, visitor);
            e += getNested(e) + 1;
        }
        visitor.endArray();
    }

    private void acceptObject(int element, Visitor visitor) {
        visitor.beginObject();
        int e = element + 1;
        while (e <= element + getNested(element)) {
            String key = getUnescapedString(e);
            visitor.onString(key);
            accept(e + 1, visitor);
            e += getNested(e + 1) + 2;
        }
        visitor.endObject();
    }

    private void parse() throws ParseException {
        try {
            int last = skipWhitespace(parseValue(0));
            if (last != input.length()) {
                throw new ParseException("malformed json");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("unbalanced json");
        }
    }

    private int parseValue(int i) throws ParseException {
        i = skipWhitespace(i);
        switch (input.charAt(i)) {
            case '"':
                return parseString(i);
            case '{':
                return parseObject(i);
            case '[':
                return parseArray(i);
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
                return parseNumber(i);
            case 't':
                return parseTrue(i);
            case 'f':
                return parseFalse(i);
            case 'n':
                return parseNull(i);
            default:
                throw new ParseException("illegal char at pos: " + i);
        }
    }

    private int parseNumber(int i) throws ParseException {
        int from = i;
        boolean minus = false;
        boolean leadingZero = false;
        boolean dot = false;
        boolean exponent = false;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '-') {
                if (i > from) {
                    throw new ParseException("minus inside number");
                }
                minus = true;
            } else if (c == 'e' || c == 'E') {
                if (exponent) {
                    throw new ParseException("double exponents");
                }
                leadingZero = false;
                exponent = true;
                c = input.charAt(i + 1);
                if (c == '-' || c == '+') {
                    c = input.charAt(i + 2);
                    if (c < '0' || c > '9') {
                        throw new ParseException("invalid exponent");
                    }
                    i += 2;
                } else if (c >= '0' && c <= '9') {
                    i++;
                } else {
                    throw new ParseException("invalid exponent");
                }
            } else if (c == '.') {
                if (dot) {
                    throw new ParseException("multiple dots");
                }
                if (i == from || (minus && (i == from + 1))) {
                    throw new ParseException("no digit before dot");
                }
                leadingZero = false;
                dot = true;
            } else if (c == '0') {
                if (i == from) {
                    leadingZero = true;
                }
            } else if (c >= '1' && c <= '9') {
                if (leadingZero) {
                    throw new ParseException("leading zero");
                }
            } else {
                break;
            }
            i++;
        }
        if (minus && from == i - 1) {
            throw new ParseException("isolated minus");
        }
        return createElement(Type.NUMBER, from, i - 1, 0);
    }

    private int parseString(int i) throws ParseException {
        boolean escaped = false;
        int from = i++;
        while (true) {
            char c = input.charAt(i);
            if (c == '"') {
                Type type = escaped ? Type.STRING_ESCAPED : Type.STRING;
                return createElement(type, from, i, 0);
            } else if (c < 32) {
                throw new ParseException("illegal control char: " + (int) c);
            } else if (c == '\\') {
                escaped = true;
                c = input.charAt(i + 1);
                if (c == '"' || c == '/' || c == '\\' || c == 'b' || c == 'f' || c == 'n' || c == 'r' || c == 't') {
                    i++;
                } else if (c == 'u') {
                    expectHex(i + 2);
                    expectHex(i + 3);
                    expectHex(i + 4);
                    expectHex(i + 5);
                    i += 5;
                } else {
                    throw new ParseException("illegal escape char: " + c);
                }
            }
            i++;
        }
    }

    private int parseArray(int i) throws ParseException {
        int count = 0;
        int e = element;
        createElement(Type.ARRAY, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (input.charAt(i) == ']') {
                return closeElement(e, i, element - e - 1);
            }
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i + 1);
            }
            i = parseValue(i);
            count++;
        }
    }

    private int parseObject(int i) throws ParseException {
        int count = 0;
        int e = element;
        createElement(Type.OBJECT, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (input.charAt(i) == '}') {
                return closeElement(e, i, element - e - 1);
            }
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i + 1);
            }
            expectChar(i, '"');
            i = parseString(i);
            i = skipWhitespace(i);
            expectChar(i, ':');
            i = skipWhitespace(i + 1);
            i = parseValue(i);
            count++;
        }
    }

    private int parseNull(int i) throws ParseException {
        expectChar(i + 1, 'u');
        expectChar(i + 2, 'l');
        expectChar(i + 3, 'l');
        return createElement(Type.NULL, i, i + 3, 0);
    }

    private int parseTrue(int i) throws ParseException {
        expectChar(i + 1, 'r');
        expectChar(i + 2, 'u');
        expectChar(i + 3, 'e');
        return createElement(Type.TRUE, i, i + 3, 0);
    }

    private int parseFalse(int i) throws ParseException {
        expectChar(i + 1, 'a');
        expectChar(i + 2, 'l');
        expectChar(i + 3, 's');
        expectChar(i + 4, 'e');
        return createElement(Type.FALSE, i, i + 4, 0);
    }

    private int skipWhitespace(int i) {
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                break;
            }
            i++;
        }
        return i;
    }

    private void expectChar(int i, char c) throws ParseException {
        if (input.charAt(i) != c) {
            throw new ParseException("expected char '" + c + "' at pos " + i);
        }
    }

    private void expectHex(int i) throws ParseException {
        char c = input.charAt(i);
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
            return;
        }
        throw new ParseException("invalid hex char at pos " + i);
    }

    private int getComponent(int element, int offset) {
        return getBlock(element)[getBlockIndex(element) + offset];
    }

    private void createElement(Type type, int from) {
        createElement(type, from, -1, -1);
    }

    private int createElement(Type type, int from, int to, int nested) {
        int currentBlock = (element * 4) / blockSize;
        if (currentBlock == blocks.size()) {
            blocks.add(new int[blockSize]);
        }
        int[] block = blocks.get(currentBlock);
        int index = getBlockIndex(element);
        block[index] = type.ordinal();
        block[index + FROM] = from;
        block[index + TO] = to;
        block[index + NESTED] = nested;
        element++;
        return to + 1;
    }

    private int closeElement(int element, int to, int nested) {
        int[] block = getBlock(element);
        int index = getBlockIndex(element);
        block[index + TO] = to;
        block[index + NESTED] = nested;
        return to + 1;
    }

    private int[] getBlock(int element) {
        return blocks.get((element * 4) / blockSize);
    }

    private int getBlockIndex(int element) {
        return (element * 4) % blockSize;
    }

}
