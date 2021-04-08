package org.xbib.datastructures.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Parser implements Closeable, Iterable<List<String>> {

    private final Lexer lexer;

    private final List<String> row;

    private final Token reusableToken;

    public Parser(Reader reader) throws IOException {
        lexer = new Lexer(new LookAheadReader(reader), ',', '\\', '"', '#', true, true);
        row = new LinkedList<>();
        reusableToken = new Token();
    }

    public Parser(Reader reader, char sep) throws IOException {
        lexer = new Lexer(new LookAheadReader(reader), sep, '\\', '"', '#', true, true);
        row = new LinkedList<>();
        reusableToken = new Token();
    }

    public long getCurrentLineNumber() {
        return lexer.getCurrentLineNumber();
    }

    @Override
    public void close() throws IOException {
        lexer.close();
    }

    @Override
    public Iterator<List<String>> iterator() {
        return new Iterator<>() {
            private List<String> current;

            private List<String> getNextRow() throws IOException {
                    return Parser.this.nextRow();
            }

            @Override
            public boolean hasNext() {
                if (current == null) {
                    try {
                        current = getNextRow();
                    } catch (IOException e) {
                        throw new NoSuchElementException(e.getMessage());
                    }
                }
                return current != null && !current.isEmpty();
            }

            @Override
            public List<String> next() {
                if (current == null || current.isEmpty()) {
                    throw new NoSuchElementException();
                }
                List<String> list = current;
                current = null;
                return list;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    protected List<String> nextRow() throws IOException {
        row.clear();
        StringBuilder sb = null;
        do {
            reusableToken.reset();
            lexer.nextToken(reusableToken);
            String s = reusableToken.content.toString();
            switch (reusableToken.type) {
                case TOKEN:
                case EORECORD:
                    row.add(s);
                    break;
                case EOF:
                    if (reusableToken.isReady) {
                        row.add(s);
                    }
                    break;
                case INVALID:
                    throw new IOException("(line " + getCurrentLineNumber() + ") invalid parse sequence");
                case COMMENT:
                    if (sb == null) {
                        sb = new StringBuilder();
                    } else {
                        sb.append(Constants.LF);
                    }
                    sb.append(reusableToken.content);
                    reusableToken.type = TokenType.TOKEN;
                    break;
                default:
                    throw new IllegalStateException("unexpected token type: " + reusableToken.type);
            }
        } while (reusableToken.type == TokenType.TOKEN);
        return row;
    }
}
