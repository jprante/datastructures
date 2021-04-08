package org.xbib.datastructures.charset;

/**
 * This class encodes and decodes Java Strings to and from the Latin-1/ISO-8859-1
 * alphabet.
 */
public class ISO88591Charset extends JavaCharset {
    
    public ISO88591Charset() {
        super("ISO-8859-1");
    }

    @Override
    public int estimateEncodeByteLength(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return 0;
        }
        // only 8-bit chars
        return stringBuilder.length();
    }

    @Override
    public int estimateDecodeCharLength(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        // only 8-bit chars
        return bytes.length;
    }
}
