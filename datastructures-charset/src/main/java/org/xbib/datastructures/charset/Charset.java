package org.xbib.datastructures.charset;

/**
 * Interface for any charset.
 */
public interface Charset {

    int estimateEncodeByteLength(StringBuilder stringBuilder);

    /**
     * Encode Java char array a byte array.
     * @param stringBuilder The Java stringbuilder to convert into a byte array
     * @return A new byte array
     */
    byte[] encode(StringBuilder stringBuilder);

    int estimateDecodeCharLength(byte[] bytes);

    /**
     * Decode the byte array to a Java string that is appended to the buffer.
     * Implementations of this method will not change any of the byte values
     * contained in the byte array.
     * @param bytes The array of bytes to decode
     * @param buffer The String buffer to append chars to
     */
    void decode(byte[] bytes, StringBuilder buffer);

    /**
     * Decode the byte array and return a new Java string.
     * Implementations of this method will not change any of the byte values
     * contained in the byte array.
     * @param bytes The array of bytes to decode
     * @return A new String with characters decoded from the byte array in
     *      the given charset.
     */
    String decode(byte[] bytes);
    
    /**
     * Normalize the characters of the source string to characters that can be
     * represented by this charset. Any characters in the input string that
     * cannot be represented by this charset are replaced with a '?' (question
     * mark character).
     * The default implementation of this method is partially inefficient by
     * first encoding the input String to a byte array representing this charset
     * followed by decoding the byte array back into a Java String. During this
     * double conversion, any characters in the original Java String that don't
     * exist in this charset are replaced with '?' (question mark characters)
     * and then decoded back into a new Java String.
     * Some charsets may choose to override this default behavior to achieve a
     * more efficient implementation.
     *
     * @param stringBuilder The source string to normalize
     * @return The normalized string
     */
    String normalize(StringBuilder stringBuilder);

}
