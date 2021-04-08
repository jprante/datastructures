package org.xbib.datastructures.charset;

/**
 * This class encodes and decodes Java Strings to and from the Latin-9/ISO-8859-15
 * alphabet.
 */
public class ISO885915Charset extends JavaCharset {
    
    public ISO885915Charset() {
        super("ISO-8859-15");
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
