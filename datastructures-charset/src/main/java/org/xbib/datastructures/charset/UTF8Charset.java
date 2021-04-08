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

    /**
     * Does not actually calculate a proper UTF-8 length, but rather a Modified
     * UTF-8 byte length.  It normally matches a real UTF-8 encoding but isn't
     * technically completely valid.
     * @deprecated 
     */
    @Deprecated
    public static int calculateByteLength(final String s) {
        return ModifiedUTF8Charset.calculateByteLength(s);
    }
}
