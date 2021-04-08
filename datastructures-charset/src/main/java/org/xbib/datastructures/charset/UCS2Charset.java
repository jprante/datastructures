package org.xbib.datastructures.charset;

/**
 * Charset for UCS2 (ISO-10646-UCS-2 in Java).
 */
public class UCS2Charset extends JavaCharset {
    
    public UCS2Charset() {
        super("ISO-10646-UCS-2");
    }

    @Override
    public int estimateEncodeByteLength(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return 0;
        }
        // let's double the estimate
        return stringBuilder.length() * 2;
    }

    @Override
    public int estimateDecodeCharLength(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        if (bytes.length % 2 == 0) {
            return bytes.length / 2;
        } else {
            return (bytes.length / 2) + 1;
        }
    }

}
