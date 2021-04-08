package org.xbib.datastructures.charset;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Charset for UCS2LE (ISO-10646-UCS-2 in Java converted to Little Endian).
 */
public class UCS2LECharset extends UCS2Charset {

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
      return getLittleEndianBytes(super.encode(stringBuilder));
    }

    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
      super.decode(getLittleEndianBytes(bytes), buffer);
    }

    @Override
    public String decode(byte[] bytes) {
      return super.decode(getLittleEndianBytes(bytes));
    }

    private byte[] getLittleEndianBytes(byte [] bytes) {
      if (bytes == null) {
          return null;
      }
      ByteBuffer sourceByteBuffer = ByteBuffer.wrap(bytes);
      ByteBuffer destByteBuffer = ByteBuffer.allocate(bytes.length);
      destByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
      while(sourceByteBuffer.hasRemaining()) {
        destByteBuffer.putShort(sourceByteBuffer.getShort());
      }
      return destByteBuffer.array();
    }
}
