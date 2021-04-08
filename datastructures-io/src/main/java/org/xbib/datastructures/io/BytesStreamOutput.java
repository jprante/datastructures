package org.xbib.datastructures.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * A growable stream of bytes, with random access methods.
 */
public class BytesStreamOutput extends OutputStream implements ByteOutput {

    /**
     * The buffer where data is stored.
     */
    private byte[] buf;

    /**
     * The number of valid bytes in the buffer.
     */
    private int count;

    public BytesStreamOutput() {
        this(1024);
    }

    /**
     * Create a new {@code BytesStreamOutput} with given buffer size.
     * @param size size
     */
    public BytesStreamOutput(int size) {
        this.buf = new byte[size];
    }

    /**
     * Write a byte.
     *
     * @param b the byte
     */
    @Override
    public void writeByte(byte b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, oversize(newcount));
        }
        buf[count] = b;
        count = newcount;
    }

    @Override
    public void write(int i) {
        byte [] b = new byte[] {
                (byte)((i >> 24) & 0xff),
                (byte)((i >> 16) & 0xff),
                (byte)((i >> 8) & 0xff),
                (byte)((i) & 0xff),
        };
        write(b, 0, 4);
    }

    /**
     * Append byte array to this output stream.
     *
     * @param b  byte array
     * @param offset offset
     * @param length length
     */
    @Override
    public void write(byte[] b, int offset, int length) {
        if (length == 0) {
            return;
        }
        int newcount = count + length;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, oversize(newcount));
        }
        System.arraycopy(b, offset, buf, count, length);
        count = newcount;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public byte[] bytes() {
        return buf;
    }

    @Override
    public void reset() {
        count = 0;
    }

    @Override
    public void flush() throws IOException {
        // nothing to do there
    }

    @Override
    public void close() throws IOException {
        // nothing to do here
    }

    /**
     * Returns an array size &gt;= minTargetSize, generally
     * over-allocating exponentially to achieve amortized
     * linear-time cost as the array grows.
     * NOTE: this was originally borrowed from Python 2.4.2
     * listobject.c sources (attribution in LICENSE.txt), but
     * has now been substantially changed based on
     * discussions from java-dev thread with subject "Dynamic
     * array reallocation algorithms", started on Jan 12
     * 2010.
     *
     * @param minTargetSize   Minimum required value to be returned.
     * @return int
     */
    private static int oversize(int minTargetSize) {
        if (minTargetSize < 0) {
            throw new IllegalArgumentException("invalid array size " + minTargetSize);
        }
        if (minTargetSize == 0) {
            return 0;
        }
        int extra = minTargetSize >> 3;
        if (extra < 3) {
            extra = 3;
        }
        int newSize = minTargetSize + extra;
        if (newSize + 7 < 0) {
            return Integer.MAX_VALUE;
        }
        return (newSize + 7) & 0x7ffffff8;
    }
}
