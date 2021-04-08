package org.xbib.datastructures.json;

import static org.xbib.datastructures.json.token.TokenType.BEGIN_ARRAY;
import static org.xbib.datastructures.json.token.TokenType.BEGIN_OBJECT;
import static org.xbib.datastructures.json.token.TokenType.END_ARRAY;
import static org.xbib.datastructures.json.token.TokenType.END_OBJECT;
import static org.xbib.datastructures.json.token.TokenType.NAME;
import static org.xbib.datastructures.json.token.TokenType.NONE;
import static org.xbib.datastructures.json.token.TokenType.NULL;
import static org.xbib.datastructures.json.token.TokenType.VALUE;
import org.xbib.datastructures.json.token.Token;
import org.xbib.datastructures.json.token.TokenType;
import org.xbib.datastructures.tiny.TinyList;
import org.xbib.datastructures.tiny.TinyMap;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Lexer {

    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final long MIN_INCOMPLETE_INTEGER = Long.MIN_VALUE / 10;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    //private static final int PEEKED_TRUE = 5;
    //private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    //private static final int PEEKED_LONG = 15;
    //private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;


    /**
     * An array with no elements requires no separators or newlines before
     * it is closed.
     */
    private static final int EMPTY_ARRAY = 1;

    /**
     * A array with at least one value requires a comma and newline before
     * the next element.
     */
    private static final int NONEMPTY_ARRAY = 2;

    /**
     * An object with no name/value pairs requires no separators or newlines
     * before it is closed.
     */
    private static final int EMPTY_OBJECT = 3;

    /**
     * An object whose most recent element is a key. The next element must
     * be a value.
     */
    private static final int DANGLING_NAME = 4;

    /**
     * An object with at least one name/value pair requires a comma and
     * newline before the next element.
     */
    private static final int NONEMPTY_OBJECT = 5;

    /**
     * No object or array has been started.
     */
    private static final int EMPTY_DOCUMENT = 6;

    /**
     * A document with at an array or object.
     */
    private static final int NONEMPTY_DOCUMENT = 7;

    /**
     * A document that's been closed and cannot be accessed.
     */
    private static final int CLOSED = 8;


    private final StringBuilder stringBuilder = new StringBuilder();

    private final char[] buffer = new char[1024];

    private Token currentToken;

    //private int peeked = PEEKED_NONE;

    private final Reader in;

    private boolean lenient = false;

    private int pos = 42;

    private int limit = 42;

    private int lineNumber = 42;

    private int lineStart = 42;

    //private long peekedLong = 123;

    //private int peekedNumberLength = 100;

    private int[] stack = new int[32];

    private int stackSize = -1;

    private StringBuilder[] pathNames = new StringBuilder[32];

    private int[] pathIndices = new int[32];
    {
        for (int i = 0; i < pathNames.length; i++) {
            pathNames[i] = new StringBuilder();
        }
    }

    public Lexer(Reader in) {
        this(in, false);
    }

    public Lexer(Reader in, boolean isLenient) {
        this.in = in;
        clear();
        setLenient(isLenient);
    }

    public Object nextObject() throws IOException {
        TokenType tokenType = next();
        switch (tokenType) {
            case BEGIN_ARRAY:
                return nextList();
            case BEGIN_OBJECT:
                return nextMap();
            //case NUMBER:
            //    return nextNumber();
            //case BOOLEAN:
            //    return nextBoolean();
            case NULL:
                nextNull();
                return null;
            default:
                return nextString();
        }
    }

    public Map<String, Object> nextMap() throws IOException {
        beginObject();
        TinyMap.Builder<String, Object> map = TinyMap.builder();
        while (hasNext()) {
            String name = nextName().toString();
            Object object = nextObject();
            map.put(name, object);
        }
        endObject();
        return map.build();
    }

    public Collection<Object> nextList() throws IOException {
        beginArray();
        TinyList.Builder<Object> list = TinyList.builder();
        while (hasNext()) {
            Object object = nextObject();
            list.add(object);
        }
        endArray();
        return list;
    }

    public void clear() {
        if (stackSize < 0) stackSize = 0;
        for (int i = 0; i < stackSize; i++) {
            pathNames[i].setLength(0);
        }
        this.stringBuilder.setLength(0);
        //this.peeked = PEEKED_NONE;
        this.currentToken = null;
        this.pos = 0;
        this.limit = 0;
        this.lineNumber = 0;
        this.lineStart = 0;
        this.stackSize = 0;
        this.stack[stackSize++] = EMPTY_DOCUMENT;
    }

    public final boolean isLenient() {
        return lenient;
    }

    public final void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public void beginArray() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        if (token.getType() == BEGIN_ARRAY) {
            push(EMPTY_ARRAY);
            pathIndices[stackSize - 1] = 0;
            currentToken = null;
        } else {
            throw new IOException("Expected BEGIN_ARRAY but was " + next() + locationString());
        }
    }

    public void endArray() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        if (token.getType() == END_ARRAY) {
            stackSize--;
            pathIndices[stackSize - 1]++;
            currentToken = null;
        } else {
            throw new IOException("Expected END_ARRAY but was " + next() + locationString());
        }
    }

    public void beginObject() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        if (token.getType() == BEGIN_OBJECT) {
            push(EMPTY_OBJECT);
            currentToken = null;
        } else {
            throw new IOException("Expected BEGIN_OBJECT but was " + next() + locationString());
        }
    }

    public void endObject() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        if (token.getType() == END_OBJECT) {
            stackSize--;
            pathNames[stackSize].setLength(0);
            pathIndices[stackSize - 1]++;
            currentToken = null;
        } else {
            throw new IOException("Expected END_OBJECT but was " + next() + locationString());
        }
    }

    public boolean hasNext() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        return token.getType() != END_OBJECT && token.getType() != END_ARRAY;
    }

    public Token next() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        return token;
        /*switch (token.getType()) {
            case PEEKED_BEGIN_OBJECT:
                return BEGIN_OBJECT;
            case PEEKED_END_OBJECT:
                return TokenType.END_OBJECT;
            case PEEKED_BEGIN_ARRAY:
                return BEGIN_ARRAY;
            case PEEKED_END_ARRAY:
                return END_ARRAY;
            case PEEKED_SINGLE_QUOTED_NAME:
            case PEEKED_DOUBLE_QUOTED_NAME:
            case PEEKED_UNQUOTED_NAME:
                return TokenType.NAME;
           // case PEEKED_TRUE:
           // case PEEKED_FALSE:
           //     return TokenType.BOOLEAN;
            case PEEKED_NULL:
                return TokenType.NULL;
            case PEEKED_SINGLE_QUOTED:
            case PEEKED_DOUBLE_QUOTED:
            case PEEKED_UNQUOTED:
            case PEEKED_BUFFERED:
                return TokenType.VALUE;
            //case PEEKED_LONG:
            //case PEEKED_NUMBER:
            //    return TokenType.NUMBER;
            default:
                assert p == PEEKED_EOF;
                return TokenType.END_DOCUMENT;
        }*/
    }

    private Token doPeek() throws IOException {
        int peekStack = stack[stackSize - 1];
        if (peekStack == EMPTY_ARRAY) {
            stack[stackSize - 1] = NONEMPTY_ARRAY;
        } else if (peekStack == NONEMPTY_ARRAY) {
            int c = nextNonWhitespace(true);
            switch (c) {
                case ']':
                    return currentToken = new Token(END_ARRAY, 0);
                case ';':
                    checkLenient();
                case ',':
                    break;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else if (peekStack == EMPTY_OBJECT || peekStack == NONEMPTY_OBJECT) {
            stack[stackSize - 1] = DANGLING_NAME;
            if (peekStack == NONEMPTY_OBJECT) {
                int c = nextNonWhitespace(true);
                switch (c) {
                    case '}':
                            return currentToken = new Token(END_OBJECT, 0);
                    case ';':
                        checkLenient();
                    case ',':
                        break;
                    default:
                        throw syntaxError("Unterminated object");
                }
            }
            int c = nextNonWhitespace(true);
            switch (c) {
                case '"':
                    //return peeked = PEEKED_DOUBLE_QUOTED_NAME;
                    return currentToken = new Token(NAME, 0);
                case '\'':
                    checkLenient();
                    //return peeked = PEEKED_SINGLE_QUOTED_NAME;
                    return currentToken = new Token(NAME, 0);
                case '}':
                    if (peekStack != NONEMPTY_OBJECT) {
                        //return peeked = PEEKED_END_OBJECT;
                        return currentToken = new Token(END_OBJECT, 0);
                    } else {
                        throw syntaxError("Expected name");
                    }
                default:
                    checkLenient();
                    pos--;
                    if (isLiteral((char) c)) {
                        //return peeked = PEEKED_UNQUOTED_NAME;
                        return currentToken = new Token(NAME, 0);
                    } else {
                        throw syntaxError("Expected name");
                    }
            }
        } else if (peekStack == DANGLING_NAME) {
            stack[stackSize - 1] = NONEMPTY_OBJECT;
            int c = nextNonWhitespace(true);
            switch (c) {
                case ':':
                    break;
                case '=':
                    checkLenient();
                    if ((pos < limit || fillBuffer(1)) && buffer[pos] == '>') {
                        pos++;
                    }
                    break;
                default:
                    throw syntaxError("Expected ':'");
            }
        } else if (peekStack == EMPTY_DOCUMENT) {
            if (lenient) {
                consumeNonExecutePrefix();
            }
            stack[stackSize - 1] = NONEMPTY_DOCUMENT;
        } else if (peekStack == NONEMPTY_DOCUMENT) {
            int c = nextNonWhitespace(false);
            if (c == -1) {
                //return peeked = PEEKED_EOF;
                return null;
            } else {
                checkLenient();
                pos--;
            }
        } else if (peekStack == CLOSED) {
            throw new IOException("closed");
        }
        int c = nextNonWhitespace(true);
        switch (c) {
            case ']':
                if (peekStack == EMPTY_ARRAY) {
                    //return peeked = PEEKED_END_ARRAY;
                    return currentToken = new Token(END_ARRAY, 0);
                }
            case ';':
            case ',':
                if (peekStack == EMPTY_ARRAY || peekStack == NONEMPTY_ARRAY) {
                    checkLenient();
                    pos--;
                    //return peeked = PEEKED_NULL;
                    return currentToken = new Token(NULL, 0);
                } else {
                    throw syntaxError("Unexpected value");
                }
            case '\'':
                checkLenient();
                //return peeked = PEEKED_SINGLE_QUOTED;
                return currentToken = new Token(NAME, 0);
            case '"':
                //return peeked = PEEKED_DOUBLE_QUOTED;
                return currentToken = new Token(NAME, 0);
            case '[':
                //return peeked = PEEKED_BEGIN_ARRAY;
                return currentToken = new Token(BEGIN_ARRAY, 0);
            case '{':
                //return peeked = PEEKED_BEGIN_OBJECT;
                return currentToken = new Token(BEGIN_OBJECT, 0);
            default:
                pos--;
        }
        Token result = peekKeyword();
        if (result != null) {
            return result;
        }
        result = peekNumber();
        if (result != null) {
            return result;
        }
        if (!isLiteral(buffer[pos])) {
            throw syntaxError("Expected value");
        }
        checkLenient();
        //return peeked = PEEKED_UNQUOTED;
        return currentToken = new Token(NAME, 0);
    }

    private Token peekKeyword() throws IOException {
        char c = buffer[pos];
        String keyword;
        String keywordUpper;
        //int peeking;
        Token token;
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            //peeking = PEEKED_TRUE;
            token = new Token(VALUE, 0, "true");
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            //peeking = PEEKED_FALSE;
            token = new Token(VALUE, 0, "false");
        } else if (c == 'n' || c == 'N') {
            keyword = "null";
            keywordUpper = "NULL";
            token = new Token(NULL, 0); //PEEKED_NULL;
        } else {
            return null;
        }
        int length = keyword.length();
        for (int i = 1; i < length; i++) {
            if (pos + i >= limit && !fillBuffer(i + 1)) {
                //return PEEKED_NONE;
                return null;
            }
            c = buffer[pos + i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                //return PEEKED_NONE;
                return null;
            }
        }
        if ((pos + length < limit || fillBuffer(length + 1))
                && isLiteral(buffer[pos + length])) {
            //return PEEKED_NONE;
            return null;
        }
        pos += length;
        return currentToken = token;
    }

    private Token peekNumber() throws IOException {
        char[] buffer = this.buffer;
        int p = pos;
        int l = limit;
        long value = 0;
        boolean negative = false;
        boolean fitsInLong = true;
        int last = NUMBER_CHAR_NONE;
        int i = 0;
        charactersOfNumber:
        for (; true; i++) {
            if (p + i == l) {
                if (i == buffer.length) {
                    return null;
                }
                if (!fillBuffer(i + 1)) {
                    break;
                }
                p = pos;
                l = limit;
            }
            char c = buffer[p + i];
            switch (c) {
                case '-':
                    if (last == NUMBER_CHAR_NONE) {
                        negative = true;
                        last = NUMBER_CHAR_SIGN;
                        continue;
                    } else if (last == NUMBER_CHAR_EXP_E) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return null;
                case '+':
                    if (last == NUMBER_CHAR_EXP_E) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return null;
                case 'e':
                case 'E':
                    if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT) {
                        last = NUMBER_CHAR_EXP_E;
                        continue;
                    }
                    return null;
                case '.':
                    if (last == NUMBER_CHAR_DIGIT) {
                        last = NUMBER_CHAR_DECIMAL;
                        continue;
                    }
                    return null;
                default:
                    if (c < '0' || c > '9') {
                        if (!isLiteral(c)) {
                            break charactersOfNumber;
                        }
                        return null;
                    }
                    if (last == NUMBER_CHAR_SIGN || last == NUMBER_CHAR_NONE) {
                        value = -(c - '0');
                        last = NUMBER_CHAR_DIGIT;
                    } else if (last == NUMBER_CHAR_DIGIT) {
                        if (value == 0) {
                            return null;
                        }
                        long newValue = value * 10 - (c - '0');
                        fitsInLong &= value > MIN_INCOMPLETE_INTEGER
                                || (value == MIN_INCOMPLETE_INTEGER && newValue < value);
                        value = newValue;
                    } else if (last == NUMBER_CHAR_DECIMAL) {
                        last = NUMBER_CHAR_FRACTION_DIGIT;
                    } else if (last == NUMBER_CHAR_EXP_E || last == NUMBER_CHAR_EXP_SIGN) {
                        last = NUMBER_CHAR_EXP_DIGIT;
                    }
            }
        }
        if (last == NUMBER_CHAR_DIGIT && fitsInLong && (value != Long.MIN_VALUE || negative) && (value != 0 || !negative)) {
            //peekedLong = negative ? value : -value;
            pos += i;
            //return peeked = PEEKED_LONG;
            //return peeked = PEEKED_UNQUOTED;
            return currentToken = new Token(VALUE, 0, String.valueOf(negative ? value : -value));
        } else if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT || last == NUMBER_CHAR_EXP_DIGIT) {
            //peekedNumberLength = i;
            //return peeked = PEEKED_NUMBER;
            //return peeked = PEEKED_UNQUOTED;
            return currentToken = new Token(VALUE, 0, String.valueOf(value));
        } else {
            return null;
        }
    }

    private boolean isLiteral(char c) throws IOException {
        switch (c) {
            case '/':
            case '\\':
            case ';':
            case '#':
            case '=':
                checkLenient();
            case '{':
            case '}':
            case '[':
            case ']':
            case ':':
            case ',':
            case ' ':
            case '\t':
            case '\f':
            case '\r':
            case '\n':
                return false;
            default:
                return true;
        }
    }

    public StringBuilder nextName() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        StringBuilder result;
        if (p == PEEKED_UNQUOTED_NAME) {
            result = nextUnquotedValue();
        } else if (p == PEEKED_SINGLE_QUOTED_NAME) {
            result = nextQuotedValue('\'');
        } else if (p == PEEKED_DOUBLE_QUOTED_NAME) {
            result = nextQuotedValue('"');
        } else {
            throw new IOException("Expected a name, but was " + next() + locationString());
        }
        peeked = PEEKED_NONE;
        StringBuilder target = pathNames[stackSize - 1];
        target.setLength(0);
        target.append(result);
        return target;
    }

    public String nextString() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        StringBuilder result = new StringBuilder();
        if (p == PEEKED_UNQUOTED) {
            result.append(nextUnquotedValue());
        } else if (p == PEEKED_SINGLE_QUOTED) {
            result.append(nextQuotedValue('\''));
        } else if (p == PEEKED_DOUBLE_QUOTED) {
            result.append(nextQuotedValue('"'));
        } else if (p == PEEKED_BUFFERED) {
            result.append(stringBuilder);
        //} else if (p == PEEKED_LONG) {
        //    result.append(stringBuilder);
        //    result.setLength(0);
        //    result.append(peekedLong);
        //} else if (p == PEEKED_NUMBER) {
        //    result.append(makeStringFromBuffer(buffer, pos, peekedNumberLength));
        //    pos += peekedNumberLength;
        } else {
            throw new IOException("Expected a string, but was " + next() + locationString());
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result.toString();
    }

    /*public boolean nextBoolean() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_TRUE) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            return true;
        } else if (p == PEEKED_FALSE) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            return false;
        }
        throw new IOException("Expected a boolean, but was " + peek() + locationString());
    }*/

    public void nextNull() throws IOException {
        Token token = currentToken;
        if (token == null) {
            token = doPeek();
        }
        if (token.getType() == NULL) {
            currentToken = null;
            pathIndices[stackSize - 1]++;
        } else {
            throw new IOException("Expected null, but was " + next() + locationString());
        }
    }

    /*public Object nextNumber() throws IOException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_LONG) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            return peekedLong;
        }
        StringBuilder builder = this.stringBuilder;
        if (p == PEEKED_NUMBER) {
            builder.setLength(0);
            builder.append(buffer, pos, peekedNumberLength);
            pos += peekedNumberLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED) {
            builder = nextQuotedValue(p == PEEKED_SINGLE_QUOTED ? '\'' : '"');
        } else if (p == PEEKED_UNQUOTED) {
            builder = nextUnquotedValue();
        } else if (p != PEEKED_BUFFERED) {
            throw new IOException("Expected a number, but was " + peek() + locationString());
        }
        peeked = PEEKED_BUFFERED;
        String s = builder.toString();
        double d = Double.parseDouble(s);
        if (!lenient && (Double.isNaN(d) || Double.isInfinite(d))) {
            throw new IOException("NaN and infinities forbidden: " + d + locationString());
        }
        builder.setLength(0);
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return d;
    }*/

    private StringBuilder makeStringFromBuffer(char[] buffer, int pos, int length) {
        stringBuilder.setLength(0);
        stringBuilder.append(buffer, pos, length);
        return stringBuilder;
    }

    private StringBuilder nextQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        StringBuilder builder = stringBuilder;
        builder.setLength(0);
        while (true) {
            int p = pos;
            int l = limit;
            int start = p;
            while (p < l) {
                int c = buffer[p++];
                if (c == quote) {
                    pos = p;
                    int len = p - start - 1;
                    builder.append(buffer, start, len);
                    return builder;
                } else if (c == '\\') {
                    pos = p;
                    int len = p - start - 1;
                    builder.append(buffer, start, len);
                    builder.append(readEscapeCharacter());
                    p = pos;
                    l = limit;
                    start = p;
                } else if (c == '\n') {
                    lineNumber++;
                    lineStart = p;
                }
            }
            builder.append(buffer, start, p - start);
            pos = p;
            if (!fillBuffer(1)) {
                throw syntaxError("Unterminated string");
            }
        }
    }

    @SuppressWarnings("fallthrough")
    private StringBuilder nextUnquotedValue() throws IOException {
        StringBuilder builder = stringBuilder;
        builder.setLength(0);
        int i = 0;
        findNonLiteralCharacter:
        while (true) {
            for (; pos + i < limit; i++) {
                switch (buffer[pos + i]) {
                    case '/':
                    case '\\':
                    case ';':
                    case '#':
                    case '=':
                        checkLenient(); // fall-through
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case ':':
                    case ',':
                    case ' ':
                    case '\t':
                    case '\f':
                    case '\r':
                    case '\n':
                        break findNonLiteralCharacter;
                }
            }
            builder.append(buffer, pos, i);
            pos += i;
            i = 0;
            if (!fillBuffer(1)) {
                break;
            }
        }
        builder.append(buffer, pos, i);
        pos += i;
        return builder;
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int i = 0;
            for (; pos + i < limit; i++) {
                switch (buffer[pos + i]) {
                    case '/':
                    case '\\':
                    case ';':
                    case '#':
                    case '=':
                        checkLenient();
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case ':':
                    case ',':
                    case ' ':
                    case '\t':
                    case '\f':
                    case '\r':
                    case '\n':
                        pos += i;
                        return;
                }
            }
            pos += i;
        } while (fillBuffer(1));
    }

    private void skipQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        do {
            int p = pos;
            int l = limit;
            while (p < l) {
                int c = buffer[p++];
                if (c == quote) {
                    pos = p;
                    return;
                } else if (c == '\\') {
                    pos = p;
                    readEscapeCharacter();
                    p = pos;
                    l = limit;
                } else if (c == '\n') {
                    lineNumber++;
                    lineStart = p;
                }
            }
            pos = p;
        } while (fillBuffer(1));
        throw syntaxError("Unterminated string");
    }

    public void skipValue() throws IOException {
        int count = 0;
        do {
            Token token = currentToken;
            if (token == null) {
                token = doPeek();
            }
            if (token.getType() == BEGIN_ARRAY) {
                push(EMPTY_ARRAY);
                count++;
            } else if (token.getType() == BEGIN_OBJECT) {
                push(EMPTY_OBJECT);
                count++;
            } else if (token.getType() == END_ARRAY) {
                stackSize--;
                count--;
            } else if (token.getType() == END_OBJECT) {
                stackSize--;
                count--;
            //} else if (token.getType() == PEEKED_UNQUOTED_NAME || p == PEEKED_UNQUOTED) {
            } else if (token.getType() == NAME && token.getValue() == null) {
                skipUnquotedValue();
            //} else if (token.getType() == NAME && token.getValue() == null) {
            } else if ((token.getType() == NAME || token.getType() == VALUE) && "'".equals(token.getValue())) {
                skipQuotedValue('\'');
            //} else if (p == PEEKED_DOUBLE_QUOTED || p == PEEKED_DOUBLE_QUOTED_NAME) {
            } else if ((token.getType() == NAME || token.getType() == VALUE) && "\"".equals(token.getValue())) {
                skipQuotedValue('"');
            //} else if (p == PEEKED_NUMBER) {
            //    pos += peekedNumberLength;
            }
            currentToken = null;
        } while (count != 0);
        pathIndices[stackSize - 1]++;
        pathNames[stackSize - 1].setLength(0);
        pathNames[stackSize - 1].append("null");
    }

    private void push(int newTop) {
        if (stackSize == stack.length) {
            int[] newStack = new int[stackSize * 2];
            int[] newPathIndices = new int[stackSize * 2];
            StringBuilder[] newPathNames = new StringBuilder[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            System.arraycopy(pathIndices, 0, newPathIndices, 0, stackSize);
            System.arraycopy(pathNames, 0, newPathNames, 0, stackSize);
            for (int i = stackSize; i < newPathNames.length; i++) {
                newPathNames[i] = new StringBuilder();
            }
            stack = newStack;
            pathIndices = newPathIndices;
            pathNames = newPathNames;
        }
        stack[stackSize++] = newTop;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        char[] buffer = this.buffer;
        lineStart -= pos;
        if (limit != pos) {
            limit -= pos;
            System.arraycopy(buffer, pos, buffer, 0, limit);
        } else {
            limit = 0;
        }
        pos = 0;
        int total;
        while ((total = in.read(buffer, limit, buffer.length - limit)) != -1) {
            limit += total;
            if (lineNumber == 0 && lineStart == 0 && limit > 0 && buffer[0] == '\ufeff') {
                pos++;
                lineStart++;
                minimum++;
            }
            if (limit >= minimum) {
                return true;
            }
        }
        return false;
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p = pos;
        int l = limit;
        while (true) {
            if (p == l) {
                pos = p;
                if (!fillBuffer(1)) {
                    break;
                }
                p = pos;
                l = limit;
            }
            int c = buffer[p++];
            if (c == '\n') {
                lineNumber++;
                lineStart = p;
                continue;
            } else if (c == ' ' || c == '\r' || c == '\t') {
                continue;
            }
            if (c == '/') {
                pos = p;
                if (p == l) {
                    pos--;
                    boolean charsLoaded = fillBuffer(2);
                    pos++;
                    if (!charsLoaded) {
                        return c;
                    }
                }
                checkLenient();
                char peek = buffer[pos];
                switch (peek) {
                    case '*':
                        pos++;
                        if (!skipTo("*/")) {
                            throw syntaxError("Unterminated comment");
                        }
                        p = pos + 2;
                        l = limit;
                        continue;
                    case '/':
                        pos++;
                        skipToEndOfLine();
                        p = pos;
                        l = limit;
                        continue;
                    default:
                        return c;
                }
            } else if (c == '#') {
                pos = p;
                checkLenient();
                skipToEndOfLine();
                p = pos;
                l = limit;
            } else {
                pos = p;
                return c;
            }
        }
        if (throwOnEof) {
            throw new EOFException("End of input" + locationString());
        } else {
            return -1;
        }
    }

    private void checkLenient() throws IOException {
        if (!lenient) {
            throw syntaxError("Use setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        while (pos < limit || fillBuffer(1)) {
            char c = buffer[pos++];
            if (c == '\n') {
                lineNumber++;
                lineStart = pos;
                break;
            } else if (c == '\r') {
                break;
            }
        }
    }

    private boolean skipTo(String toFind) throws IOException {
        int length = toFind.length();
        outer:
        for (; pos + length <= limit || fillBuffer(length); pos++) {
            if (buffer[pos] == '\n') {
                lineNumber++;
                lineStart = pos + 1;
                continue;
            }
            for (int c = 0; c < length; c++) {
                if (buffer[pos + c] != toFind.charAt(c)) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    private String locationString() {
        int line = lineNumber + 1;
        int column = pos - lineStart + 1;
        return " at line " + line + " column " + column + " path " + getPath();
    }

    public StringBuilder getPath() {
        StringBuilder result = stringBuilder;
        result.setLength(0);
        result.append('$');
        for (int i = 0, size = stackSize; i < size; i++) {
            switch (stack[i]) {
                case EMPTY_ARRAY:
                case NONEMPTY_ARRAY:
                    result.append('[').append(pathIndices[i]).append(']');
                    break;

                case EMPTY_OBJECT:
                case DANGLING_NAME:
                case NONEMPTY_OBJECT:
                    result.append('.');
                    if (pathNames[i].length() > 0) {
                        result.append(pathNames[i]);
                    }
                    break;

                case NONEMPTY_DOCUMENT:
                case EMPTY_DOCUMENT:
                case CLOSED:
                    break;
            }
        }
        return result;
    }

    private char readEscapeCharacter() throws IOException {
        if (pos == limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }
        char escaped = buffer[pos++];
        switch (escaped) {
            case 'u':
                if (pos + 4 > limit && !fillBuffer(4)) {
                    throw syntaxError("Unterminated escape sequence");
                }
                char result = 0;
                for (int i = pos, end = i + 4; i < end; i++) {
                    char c = buffer[i];
                    result <<= 4;
                    if (c >= '0' && c <= '9') {
                        result += (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        result += (c - 'a' + 10);
                    } else if (c >= 'A' && c <= 'F') {
                        result += (c - 'A' + 10);
                    } else {
                        throw new NumberFormatException("\\u" + makeStringFromBuffer(buffer, pos, 4));
                    }
                }
                pos += 4;
                return result;
            case 't':
                return '\t';
            case 'b':
                return '\b';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 'f':
                return '\f';
            case '\n':
                lineNumber++;
                lineStart = pos;
            case '\'':
            case '"':
            case '\\':
            case '/':
                return escaped;
            default:
                throw syntaxError("Invalid escape sequence");
        }
    }

    private IOException syntaxError(String message) throws IOException {
        throw new IOException(message + locationString());
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace(true);
        pos--;
        if (pos + NON_EXECUTE_PREFIX.length > limit && !fillBuffer(NON_EXECUTE_PREFIX.length)) {
            return;
        }
        for (int i = 0; i < NON_EXECUTE_PREFIX.length; i++) {
            if (buffer[pos + i] != NON_EXECUTE_PREFIX[i]) {
                return; // not a security token!
            }
        }
        pos += NON_EXECUTE_PREFIX.length;
    }
}
