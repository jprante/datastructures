package org.xbib.datastructures.io;

public interface ByteOutput {

    void writeByte(byte b);

    void write(byte[] b, int offset, int count);

    int count();

    byte[] bytes();

    void reset();
}
