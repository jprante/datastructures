package org.xbib.datastructures.common;

import java.nio.charset.StandardCharsets;

public class CompactCharSequence implements CharSequence {

    private final int offset;

    private final int length;

    private final byte[] bytes;

    public CompactCharSequence(String string) {
        this.bytes = string.getBytes(StandardCharsets.ISO_8859_1);
        this.offset = 0;
        this.length = bytes.length;
    }

    public CompactCharSequence(CompactCharSequence charSequence, int offset, int length) {
        this.bytes = charSequence.bytes;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public char charAt(int index) {
        int ix = offset + index;
        if (ix >= length) {
            throw new StringIndexOutOfBoundsException("Invalid index " + index + " length " + length());
        }
        return (char) (bytes[ix] & 0xff);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CompactCharSequence(this, start, end- start);
    }

    @Override
    public int length() {
        return length;
    }
}