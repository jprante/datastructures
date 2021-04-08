package org.xbib.datastructures.charset;

import org.xbib.datastructures.common.FastByteArrayOutputStream;
import java.io.IOException;

/**
 * This class encodes and decodes Java Strings to and from the SMS default
 * alphabet. It also supports the default extension table. The default alphabet
 * and it's extension table is defined in GSM 03.38.
 *
 */
public class GSMCharset extends BaseCharset {

    public static final int EXTENDED_ESCAPE = 0x1b;

    public static final char[] CHAR_TABLE = {
        '@', '\u00a3', '$', '\u00a5', '\u00e8', '\u00e9', '\u00f9', '\u00ec',
        '\u00f2', '\u00c7', '\n', '\u00d8', '\u00f8', '\r', '\u00c5', '\u00e5',
        '\u0394', '_', '\u03a6', '\u0393', '\u039b', '\u03a9', '\u03a0', '\u03a8',
        '\u03a3', '\u0398', '\u039e', ' ', '\u00c6', '\u00e6', '\u00df', '\u00c9',  // 0x1B is actually an escape which we'll encode to a space char
        ' ', '!', '"', '#', '\u00a4', '%', '&', '\'',
        '(', ')', '*', '+', ',', '-', '.', '/',
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', ':', ';', '<', '=', '>', '?',
        '\u00a1', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
        'X', 'Y', 'Z', '\u00c4', '\u00d6', '\u00d1', '\u00dc', '\u00a7',
        '\u00bf', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
        'x', 'y', 'z', '\u00e4', '\u00f6', '\u00f1', '\u00fc', '\u00e0',
    };

    /**
     * Extended character table. Characters in this table are accessed by the
     * 'escape' character in the base table. It is important that none of the
     * 'inactive' characters ever be matchable with a valid base-table
     * character as this breaks the encoding loop.
     *
     * @see #EXTENDED_ESCAPE
     */
    public static final char[] EXT_CHAR_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, '\f', 0, 0, 0, 0, 0,
            0, 0, 0, 0, '^', 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            '{', '}', 0, 0, 0, 0, 0, '\\',
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, '[', '~', ']', 0,
            '|', 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, '\u20ac', 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };
    
    /**
     * Verifies that this charset can represent every character in the Java
     * String (char sequence).
     * @param str0 The String to verify
     * @return True if the charset can represent every character in the Java
     *      String, otherwise false.
     */
    static public boolean canRepresent(CharSequence str0) {
        if (str0 == null) {
            return true;
        }

        int len = str0.length();
        for (int i = 0; i < len; i++) {
            // get the char in this string
            char c = str0.charAt(i);
            // simple range checks for most common characters (0x20 -> 0x5F) or (0x61 -> 0x7E)
            if ((c >= ' ' && c <= '_') || (c >= 'a' && c <= '~')) {
                continue;
            } else {
                // 10X more efficient using a switch statement vs. a lookup table search
                switch (c) {
                    case '\u00A3':	// £
                    case '\u00A5':	// ¥
                    case '\u00E8':	// è
                    case '\u00E9':	// é
                    case '\u00F9':	// ù
                    case '\u00EC':	// ì
                    case '\u00F2':	// ò
                    case '\u00C7':	// Ç
                    case '\n':          // newline
                    case '\u00D8':	// Ø
                    case '\u00F8':	// ø
                    case '\r':          // carriage return
                    case '\u000c':      // form feed
                    case '\u00C5':	// Å
                    case '\u00E5':	// å
                    case '\u0394':	// Δ
                    case '\u03A6':	// Φ
                    case '\u0393':	// Γ
                    case '\u039B':	// Λ
                    case '\u03A9':	// Ω
                    case '\u03A0':	// Π
                    case '\u03A8':	// Ψ
                    case '\u03A3':	// Σ
                    case '\u0398':	// Θ
                    case '\u039E':	// Ξ
                    case '\u00C6':	// Æ
                    case '\u00E6':	// æ
                    case '\u00DF':	// ß
                    case '\u00C9':	// É
                    case '\u00A4':	// ¤
                    case '\u00A1':	// ¡
                    case '\u00C4':	// Ä
                    case '\u00D6':	// Ö
                    case '\u00D1':	// Ñ
                    case '\u00DC':	// Ü
                    case '\u00A7':	// §
                    case '\u00BF':	// ¿
                    case '\u00E4':	// ä
                    case '\u00F6':	// ö
                    case '\u00F1':	// ñ
                    case '\u00FC':	// ü
                    case '\u00E0':	// à
                    case '\u20AC':	// €
                        continue;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public int estimateEncodeByteLength(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return 0;
        }
        // only a couple chars are expected to be "double" bytes
        return stringBuilder.length() + 10;
    }

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return null;
        }
        int estimatedByteLength = estimateEncodeByteLength(stringBuilder);
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream(estimatedByteLength);
        try {
            for (int i = 0; i < stringBuilder.length(); i++) {
                int search = 0;
                char c = stringBuilder.charAt(i);
                for (; search < CHAR_TABLE.length; search++) {
                    if (search == EXTENDED_ESCAPE) {
                        continue;
                    }
                    if (c == CHAR_TABLE[search]) {
                        baos.write(search);
                        break;
                    }
                    if (c == EXT_CHAR_TABLE[search]) {
                        baos.write(EXTENDED_ESCAPE);
                        baos.write(search);
                        break;
                    }
                }
                if (search == CHAR_TABLE.length) {
                    // A '?' character.
                    baos.write(0x3f);
                }
            }
        } catch (IOException e) {
            // should be an impossible error
            throw new RuntimeException("Impossible error with FastByteArrayOutputStream: " + e.getMessage(), e);
        }

        return baos.toByteArray();

    }

    @Override
    public int estimateDecodeCharLength(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        if (bytes.length < 2) {
            return bytes.length;
        }
        // only a couple chars are expected to be "double" bytes
        return bytes.length + 10;
    }

    /**
     * Decode an SMS default alphabet-encoded octet string into a Java String.
     */
    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
        if (bytes == null) {
            // append nothing
            return;
        }

        char[] table = CHAR_TABLE;
        for (int i = 0; i < bytes.length; i++) {
            int code = (int)bytes[i] & 0x000000ff;
            if (code == EXTENDED_ESCAPE) {
                // take next char from extension table
                table = EXT_CHAR_TABLE;
            } else {
                buffer.append((code >= table.length) ? '?' : table[code]);
                // go back to the default table
                table = CHAR_TABLE;
            }
        }
    }
}
