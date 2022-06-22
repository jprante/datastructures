package org.xbib.datastructures.csv;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Generator implements Constants, Closeable, Flushable {

    private static final String LF = System.getProperty("line.separator");

    private final Writer writer;

    private int col;

    private int row;

    private List<String> keys;

    private char separator = COMMA;

    private char quote = DOUBLE_QUOTE;

    private boolean alwaysQuote;

    public Generator(Writer writer) {
        this.writer = writer;
        this.col = 0;
        this.keys = new ArrayList<>();
    }

    public Generator setSeparator(char separator) {
        this.separator = separator;
        return this;
    }

    public Generator setAlwaysQuote(boolean alwaysQuote) {
        this.alwaysQuote = alwaysQuote;
        return this;
    }

    public Generator setQuote(char quote) {
        this.quote = quote;
        return this;
    }

    public Generator keys(List<String> keys) {
        this.keys = keys;
        return this;
    }

    public Generator writeKeys() throws IOException {
        for (String k : keys) {
            write(k);
        }
        return this;
    }

    public synchronized void write(String value) throws IOException {
        if (col >= keys.size()) {
            writer.write(LF);
            row++;
            col = 0;
        } else {
            if (col > 0) {
                writer.write(separator);
            }
        }
        if (value != null) {
            writer.write(escape(value));
        }
        col++;
    }

    public int getColumn() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    private String escape(String value) {
        if (!alwaysQuote && value.indexOf(quote) < 0
                && value.indexOf(COMMA) < 0
                && value.indexOf(TAB) < 0
                && !value.contains(LF)) {
           return value;
        }
        int length = value.length();
        StringBuilder sb = new StringBuilder(length + 2);
        sb.append(quote);
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (ch == quote) {
                sb.append(quote);
            }
            sb.append(ch);
        }
        sb.append(quote);
        return sb.toString();
    }

}
