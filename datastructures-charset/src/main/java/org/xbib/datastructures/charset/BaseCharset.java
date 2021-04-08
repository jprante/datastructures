package org.xbib.datastructures.charset;

/**
 * Base Charset class implementing common functionality.
 */
public abstract class BaseCharset implements Charset {

    /**
     * Default implementation that simply returns a String by creating a new
     * StringBuffer, appending to it, and then returning a new String.  NOTE:
     * This method is NOT efficient since it requires a double copy of a new
     * String.  Some charsets will override this default implementation to
     * provide a more efficient impl.
     */
    @Override
    public String decode(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder(estimateDecodeCharLength(bytes));
        decode(bytes, buffer);
        return buffer.toString();
    }
    
    @Override
    public String normalize(StringBuilder stringBuilder) {
        byte[] bytes = this.encode(stringBuilder);
        // normalizing a string should never be result in a longer string
        StringBuilder buf = new StringBuilder(bytes.length);
        this.decode(bytes, buf);
        return buf.toString();
    }

}
