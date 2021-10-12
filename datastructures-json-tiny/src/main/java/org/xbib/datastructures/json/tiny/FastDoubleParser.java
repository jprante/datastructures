package org.xbib.datastructures.json.tiny;

public class FastDoubleParser {

    private final static long MINIMAL_NINETEEN_DIGIT_INTEGER = 1000_00000_00000_00000L;

    private final static int MINIMAL_EIGHT_DIGIT_INTEGER = 10_000_000;

    private static final byte DECIMAL_POINT_CLASS = -4;

    private static final byte OTHER_CLASS = -1;

    private static final byte[] CHAR_TO_HEX_MAP = new byte[256];

    static {
        for (char ch = 0; ch < CHAR_TO_HEX_MAP.length; ch++) {
            CHAR_TO_HEX_MAP[ch] = OTHER_CLASS;
        }
        for (char ch = '0'; ch <= '9'; ch++) {
            CHAR_TO_HEX_MAP[ch] = (byte) (ch - '0');
        }
        for (char ch = 'A'; ch <= 'F'; ch++) {
            CHAR_TO_HEX_MAP[ch] = (byte) (ch - 'A' + 10);
        }
        for (char ch = 'a'; ch <= 'f'; ch++) {
            CHAR_TO_HEX_MAP[ch] = (byte) (ch - 'a' + 10);
        }
        for (char ch = '.'; ch <= '.'; ch++) {
            CHAR_TO_HEX_MAP[ch] = DECIMAL_POINT_CLASS;
        }
    }

    private FastDoubleParser() {
    }

    private static boolean isInteger(char c) {
        return '0' <= c && c <= '9';
    }

    private static NumberFormatException newNumberFormatException(CharSequence str) {
        if (str.length() > 1024) {
            return new NumberFormatException("For input string of length " + str.length());
        } else {
            return new NumberFormatException("For input string: \"" + str.toString().trim() + "\"");
        }
    }

    public static double parseDouble(CharSequence str) throws NumberFormatException {
        final int endIndex = str.length();
        int index = skipWhitespace(str, 0, endIndex);
        if (index == endIndex) {
            throw new NumberFormatException("empty String");
        }
        char ch = str.charAt(index);
        final boolean isNegative = ch == '-';
        if (isNegative || ch == '+') {
            ch = ++index < endIndex ? str.charAt(index) : 0;
            if (ch == 0) {
                throw newNumberFormatException(str);
            }
        }
        if (ch == 'N') {
            return parseNaN(str, index, endIndex);
        } else if (ch == 'I') {
            return parseInfinity(str, index, endIndex, isNegative);
        }
        final boolean hasLeadingZero = ch == '0';
        if (hasLeadingZero) {
            ch = ++index < endIndex ? str.charAt(index) : 0;
            if (ch == 'x' || ch == 'X') {
                return parseRestOfHexFloatingPointLiteral(str, index + 1, endIndex, isNegative);
            }
        }
        return parseRestOfDecimalFloatLiteral(str, endIndex, index, isNegative, hasLeadingZero);
    }

    private static double parseInfinity(CharSequence str, int index, int endIndex, boolean negative) {
        if (index + 7 < endIndex
                && str.charAt(index + 1) == 'n'
                && str.charAt(index + 2) == 'f'
                && str.charAt(index + 3) == 'i'
                && str.charAt(index + 4) == 'n'
                && str.charAt(index + 5) == 'i'
                && str.charAt(index + 6) == 't'
                && str.charAt(index + 7) == 'y'
        ) {
            index = skipWhitespace(str, index + 8, endIndex);
            if (index < endIndex) {
                throw newNumberFormatException(str);
            }
            return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        } else {
            throw newNumberFormatException(str);
        }
    }

    private static double parseNaN(CharSequence str, int index, int endIndex) {
        if (index + 2 < endIndex
                && str.charAt(index + 1) == 'a'
                && str.charAt(index + 2) == 'N') {
            index = skipWhitespace(str, index + 3, endIndex);
            if (index < endIndex) {
                throw newNumberFormatException(str);
            }
            return Double.NaN;
        } else {
            throw newNumberFormatException(str);
        }
    }

    private static double parseRestOfDecimalFloatLiteral(CharSequence str, int endIndex, int index, boolean isNegative, boolean hasLeadingZero) {
        long digits = 0;
        int exponent = 0;
        final int indexOfFirstDigit = index;
        int virtualIndexOfPoint = -1;
        final int digitCount;
        char ch = 0;
        for (; index < endIndex; index++) {
            ch = str.charAt(index);
            if (isInteger(ch)) {
                digits = 10 * digits + ch - '0';
            } else if (ch == '.') {
                if (virtualIndexOfPoint != -1) {
                    throw newNumberFormatException(str);
                }
                virtualIndexOfPoint = index;
            } else {
                break;
            }
        }
        final int indexAfterDigits = index;
        if (virtualIndexOfPoint == -1) {
            digitCount = indexAfterDigits - indexOfFirstDigit;
            virtualIndexOfPoint = indexAfterDigits;
        } else {
            digitCount = indexAfterDigits - indexOfFirstDigit - 1;
            exponent = virtualIndexOfPoint - index + 1;
        }
        long exp_number = 0;
        final boolean hasExponent = (ch == 'e') || (ch == 'E');
        if (hasExponent) {
            ch = ++index < endIndex ? str.charAt(index) : 0;
            boolean neg_exp = ch == '-';
            if (neg_exp || ch == '+') {
                ch = ++index < endIndex ? str.charAt(index) : 0;
            }
            if (!isInteger(ch)) {
                throw newNumberFormatException(str);
            }
            do {
                if (exp_number < MINIMAL_EIGHT_DIGIT_INTEGER) {
                    exp_number = 10 * exp_number + ch - '0';
                }
                ch = ++index < endIndex ? str.charAt(index) : 0;
            } while (isInteger(ch));
            if (neg_exp) {
                exp_number = -exp_number;
            }
            exponent += exp_number;
        }
        index = skipWhitespace(str, index, endIndex);
        if (index < endIndex
                || !hasLeadingZero && digitCount == 0 && str.charAt(virtualIndexOfPoint) != '.') {
            throw newNumberFormatException(str);
        }
        final boolean isDigitsTruncated;
        int skipCountInTruncatedDigits = 0;//counts +1 if we skipped over the decimal point
        if (digitCount > 19) {
            digits = 0;
            for (index = indexOfFirstDigit; index < indexAfterDigits; index++) {
                ch = str.charAt(index);
                if (ch == '.') {
                    skipCountInTruncatedDigits++;
                } else {
                    if (Long.compareUnsigned(digits, MINIMAL_NINETEEN_DIGIT_INTEGER) < 0) {
                        digits = 10 * digits + ch - '0';
                    } else {
                        break;
                    }
                }
            }
            isDigitsTruncated = (index < indexAfterDigits);
        } else {
            isDigitsTruncated = false;
        }
        Double result = FastDoubleMath.decFloatLiteralToDouble(index, isNegative, digits, exponent, virtualIndexOfPoint, exp_number, isDigitsTruncated, skipCountInTruncatedDigits);
        if (result == null) {
            return parseRestOfDecimalFloatLiteralTheHardWay(str);
        }
        return result;
    }

    /**
     * Parses the following rules
     * (more rules are defined in {@link #parseDouble(CharSequence)}):
     * <dl>
     * <dt><i>RestOfDecimalFloatingPointLiteral</i>:
     * <dd><i>[Digits] {@code .} [Digits] [ExponentPart]</i>
     * <dd><i>{@code .} Digits [ExponentPart]</i>
     * <dd><i>[Digits] ExponentPart</i>
     * </dl>
     *  @param str            the input string
     */
    private static double parseRestOfDecimalFloatLiteralTheHardWay(CharSequence str) {
        return Double.parseDouble(str.toString());
    }

    /**
     * Parses the following rules
     * (more rules are defined in {@link #parseDouble(CharSequence)}):
     * <dl>
     * <dt><i>RestOfHexFloatingPointLiteral</i>:
     * <dd><i>RestOfHexSignificand BinaryExponent</i>
     * </dl>
     *
     * <dl>
     * <dt><i>RestOfHexSignificand:</i>
     * <dd><i>HexDigits</i>
     * <dd><i>HexDigits</i> {@code .}
     * <dd><i>[HexDigits]</i> {@code .} <i>HexDigits</i>
     * </dl>
     *
     * @param str        the input string
     * @param index      index to the first character of RestOfHexFloatingPointLiteral
     * @param endIndex   the end index of the string
     * @param isNegative if the resulting number is negative
     * @return a double representation
     */
    private static double parseRestOfHexFloatingPointLiteral(
            CharSequence str, int index, int endIndex, boolean isNegative) {
        if (index >= endIndex) {
            throw newNumberFormatException(str);
        }

        // Parse digits
        // ------------
        long digits = 0;// digits is treated as an unsigned long
        int exponent = 0;
        final int indexOfFirstDigit = index;
        int virtualIndexOfPoint = -1;
        final int digitCount;
        char ch = 0;
        for (; index < endIndex; index++) {
            ch = str.charAt(index);
            // Table look up is faster than a sequence of if-else-branches.
            int hexValue = ch > 255 ? OTHER_CLASS : CHAR_TO_HEX_MAP[ch];
            if (hexValue >= 0) {
                digits = (digits << 4) | hexValue;// This might overflow, we deal with it later.
            } else if (hexValue == DECIMAL_POINT_CLASS) {
                if (virtualIndexOfPoint != -1) {
                    throw newNumberFormatException(str);
                }
                virtualIndexOfPoint = index;
            } else {
                break;
            }
        }
        final int indexAfterDigits = index;
        if (virtualIndexOfPoint == -1) {
            digitCount = indexAfterDigits - indexOfFirstDigit;
            virtualIndexOfPoint = indexAfterDigits;
        } else {
            digitCount = indexAfterDigits - indexOfFirstDigit - 1;
            exponent = Math.min(virtualIndexOfPoint - index + 1, MINIMAL_EIGHT_DIGIT_INTEGER) * 4;
        }

        // Parse exponent number
        // ---------------------
        long exp_number = 0;
        final boolean hasExponent = (ch == 'p') || (ch == 'P');
        if (hasExponent) {
            ch = ++index < endIndex ? str.charAt(index) : 0;
            boolean neg_exp = ch == '-';
            if (neg_exp || ch == '+') {
                ch = ++index < endIndex ? str.charAt(index) : 0;
            }
            if (!isInteger(ch)) {
                throw newNumberFormatException(str);
            }
            do {
                // Guard against overflow of exp_number
                if (exp_number < MINIMAL_EIGHT_DIGIT_INTEGER) {
                    exp_number = 10 * exp_number + ch - '0';
                }
                ch = ++index < endIndex ? str.charAt(index) : 0;
            } while (isInteger(ch));
            if (neg_exp) {
                exp_number = -exp_number;
            }
            exponent += exp_number;
        }

        // Skip trailing whitespace
        // ------------------------
        index = skipWhitespace(str, index, endIndex);
        if (index < endIndex
                || digitCount == 0 && str.charAt(virtualIndexOfPoint) != '.'
                || !hasExponent) {
            throw newNumberFormatException(str);
        }

        // Re-parse digits in case of a potential overflow
        // -----------------------------------------------
        final boolean isDigitsTruncated;
        int skipCountInTruncatedDigits = 0;//counts +1 if we skipped over the decimal point
        if (digitCount > 16) {
            digits = 0;
            for (index = indexOfFirstDigit; index < indexAfterDigits; index++) {
                ch = str.charAt(index);
                // Table look up is faster than a sequence of if-else-branches.
                int hexValue = ch > 127 ? OTHER_CLASS : CHAR_TO_HEX_MAP[ch];
                if (hexValue >= 0) {
                    if (Long.compareUnsigned(digits, MINIMAL_NINETEEN_DIGIT_INTEGER) < 0) {
                        digits = (digits << 4) | hexValue;
                    } else {
                        break;
                    }
                } else {
                    skipCountInTruncatedDigits++;
                }
            }
            isDigitsTruncated = (index < indexAfterDigits);
        } else {
            isDigitsTruncated = false;
        }

        Double d = FastDoubleMath.hexFloatLiteralToDouble(index, isNegative, digits, exponent, virtualIndexOfPoint, exp_number, isDigitsTruncated, skipCountInTruncatedDigits);
        return d == null ? Double.parseDouble(str.toString()) : d;
    }

    private static int skipWhitespace(CharSequence str, int index, int endIndex) {
        for (; index < endIndex; index++) {
            if (str.charAt(index) > 0x20) {
                break;
            }
        }
        return index;
    }
}