package org.xbib.datastructures.charset;

/**
 * Charset for UTF-8.
 *
 */
public class UTF8Charset extends JavaCharset {
    
    public UTF8Charset() {
        super("UTF8");
    }

    @Override
    public int estimateEncodeByteLength(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return 0;
        }
        return stringBuilder.length() * 2;
    }

    @Override
    public int estimateDecodeCharLength(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        return bytes.length;
    }
}
