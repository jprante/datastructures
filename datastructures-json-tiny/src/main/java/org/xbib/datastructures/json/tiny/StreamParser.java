package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.api.Parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

public class StreamParser implements Parser {

    private final JsonResult listener;

    private Reader reader;

    private int ch;

    public StreamParser() {
        this(new TinyJsonListener());
    }

    public StreamParser(JsonResult listener) {
        this.listener = listener;
    }

    @Override
    public Node<?> parse(Reader reader) throws IOException {
        Objects.requireNonNull(reader);
        Objects.requireNonNull(listener);
        this.reader = reader;
        listener.begin();
        ch = reader.read();
        skipWhitespace();
        parseValue();
        skipWhitespace();
        if (ch != -1) {
            throw new JsonException("malformed json: " + ch);
        }
        listener.end();
        return listener.getResult();
    }

    private void parseValue() throws IOException, JsonException {
        switch (ch) {
            case '"':
                parseString(false);
                break;
            case '{':
                parseMap();
                break;
            case '[':
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

    private void parseNumber() throws IOException, JsonException {
        boolean minus = false;
        boolean dot = false;
        boolean exponent = false;
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (ch == '-') {
                if (sb.length() > 1) {
                    throw new JsonException("minus inside number");
                }
                sb.append((char) ch);
                ch = reader.read();
                minus = true;
            } else if (ch == 'e' || ch == 'E') {
                sb.append((char) ch);
                ch = reader.read();
                if (exponent) {
                    throw new JsonException("double exponents");
                }
                exponent = true;
                ch = reader.read();
                if (ch == '-' || ch == '+') {
                    ch = reader.read();
                    if (ch < '0' || ch > '9') {
                        throw new JsonException("invalid exponent");
                    }
                } else if (ch < '0' || ch > '9') {
                    throw new JsonException("invalid exponent");
                }
            } else if (ch == '.') {
                sb.append((char) ch);
                ch = reader.read();
                if (dot) {
                    throw new JsonException("multiple dots");
                }
                if (sb.length() == 1) {
                    throw new JsonException("no digit before dot");
                }
                dot = true;
            } else if (ch >= '0' && ch <= '9') {
                sb.append((char) ch);
                ch = reader.read();
            } else {
                break;
            }
        }
        if (minus && sb.length() == 1) {
            throw new JsonException("isolated minus");
        }
        if (dot || exponent) {
            listener.onDouble(FastDoubleParser.parseDouble(sb));
        } else {
            listener.onLong(Long.parseLong(sb.toString()));
        }
    }

    private void parseString(boolean isKey) throws IOException, JsonException {
        reader.mark(1024);
        ch = reader.read();
        boolean escaped = false;
        int count = 1;
        while (true) {
            if (ch == '"') {
                char[] buffer = new char[count - 1];
                reader.reset();
                reader.read(buffer, 0, count - 1);
                reader.read();
                CharSequence s = new String(buffer);
                if (escaped) {
                    s = unescape(s);
                    if (isKey) {
                        listener.onKey(s);
                    } else {
                        listener.onValue(s);
                    }
                } else {
                    if (isKey) {
                        listener.onKey(s);
                    } else {
                        listener.onValue(s);
                    }
                }
                ch = reader.read();
                return;
            } else if (ch == '\\') {
                escaped = true;
                ch = reader.read();
                if (ch == '"' || ch == '/' || ch == '\\' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't') {
                    count++;
                    ch = reader.read();
                } else if (ch == 'u') {
                    expectHex();
                    expectHex();
                    expectHex();
                    expectHex();
                    count += 5;
                } else {
                    throw new JsonException("illegal escape char: " + ch);
                }
            } else if (ch < 32) {
                throw new JsonException("illegal control char: " + ch);
            } else {
                count++;
                ch = reader.read();
            }
        }
    }

    private void parseList() throws IOException {
        int count = 0;
        listener.beginCollection();
        ch = reader.read();
        while (true) {
            skipWhitespace();
            if (ch == ']') {
                listener.endCollection();
                ch = reader.read();
                return;
            }
            if (count > 0) {
                expectChar(',');
                ch = reader.read();
                skipWhitespace();
            }
            parseValue();
            count++;
        }
    }

    private void parseMap() throws IOException, JsonException {
        int count = 0;
        listener.beginMap();
        ch = reader.read();
        while (true) {
            skipWhitespace();
            if (ch == '}') {
                listener.endMap();
                ch = reader.read();
                return;
            }
            if (count > 0) {
                expectChar(',');
                ch = reader.read();
                skipWhitespace();
            }
            expectChar('"');
            parseString(true);
            skipWhitespace();
            expectChar(':');
            ch = reader.read();
            skipWhitespace();
            parseValue();
            count++;
        }
    }

    private void parseNull() throws IOException, JsonException {
        ch = reader.read();
        expectChar('u');
        ch = reader.read();
        expectChar('l');
        ch = reader.read();
        expectChar('l');
        listener.onNull();
        ch = reader.read();
    }

    private void parseTrue() throws IOException, JsonException {
        ch = reader.read();
        expectChar('r');
        ch = reader.read();
        expectChar('u');
        ch = reader.read();
        expectChar('e');
        listener.onTrue();
        ch = reader.read();
    }

    private void parseFalse() throws IOException, JsonException {
        ch = reader.read();
        expectChar('a');
        ch = reader.read();
        expectChar('l');
        ch = reader.read();
        expectChar('s');
        ch = reader.read();
        expectChar('e');
        listener.onFalse();
        ch = reader.read();
    }

    private void expectChar(char expected) throws JsonException {
        if (ch != expected) {
            throw new JsonException("expected char " + (char)expected + " but got " + (char)ch);
        }
    }

    private void expectHex() throws IOException, JsonException {
        ch = reader.read();
        if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
            return;
        }
        throw new JsonException("invalid hex char " + ch);
    }

    private void skipWhitespace() throws IOException {
        while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            ch = reader.read();
        }
    }

    private static CharSequence unescape(CharSequence input) {
        StringBuilder result = new StringBuilder(input.length());
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '\\') {
                i++;
                switch (input.charAt(i)) {
                    case '\\':
                        result.append('\\');
                        break;
                    case '/':
                        result.append('/');
                        break;
                    case '"':
                        result.append('"');
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
}
