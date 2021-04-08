package org.xbib.datastructures.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Base class for all charset conversions that rely on the internal Java
 * String getBytes() methods.
 * 
 */
public abstract class JavaCharset extends BaseCharset {
    
    private final java.nio.charset.Charset charset;

    public JavaCharset(String charsetName) {
        try {
            this.charset = java.nio.charset.Charset.forName(charsetName);
        } catch (UnsupportedCharsetException e) {
            throw new IllegalArgumentException("Unsupported Java charset [" + charsetName + "]");
        }
    }

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return null;
        }
        return stringBuilder.toString().getBytes(charset);
    }

    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
        if (bytes == null) {
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer charBuffer = charset.decode(byteBuffer);
        buffer.append(charBuffer);
    }
    
    @Override
    public String decode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

}
