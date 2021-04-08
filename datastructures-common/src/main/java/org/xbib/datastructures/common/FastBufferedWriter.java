package org.xbib.datastructures.common;

import java.io.IOException;
import java.io.Writer;

public final class FastBufferedWriter extends Writer {

    private static final int BUFFER_SIZE = 8192;

    private final Writer writer;

    private final char[] chars;

    private int pos;

    public FastBufferedWriter(Writer writer) {
        this.writer = writer;
        this.chars = new char[BUFFER_SIZE];
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if (pos + len >= chars.length) {
            flushBuffer();
        }
        if (len >= chars.length) {
            writer.write(cbuf, off, len);
        } else {
            System.arraycopy(cbuf, off, chars, pos, len);
            pos += len;
        }
    }

    @Override
    public void write(final int c) throws IOException {
        if (pos == chars.length) {
            flushBuffer();
        }
        chars[pos++] = (char) c;
    }

    @Override
    public void close() throws IOException {
        flushBuffer();
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        flushBuffer();
        writer.flush();
    }

    private void flushBuffer() throws IOException {
        writer.write(chars, 0, pos);
        pos = 0;
    }

    @Override
    public String toString() {
        return writer.toString();
    }


}
