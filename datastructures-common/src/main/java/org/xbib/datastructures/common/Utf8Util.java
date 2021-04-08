package org.xbib.datastructures.common;

public class Utf8Util {

    public static String decode(byte[] data, int offset, int length) {
        char[] chars = new char[length];
        int len = 0;
        int i = offset;
        while (i < length) {
            if ((data[i] & 0x80) == 0) {
                chars[len] = (char) data[i];
                len++;
                i++;
            } else {
                int uc = 0;
                if ((data[i] & 0xE0) == 0xC0) {
                    uc = (data[i] & 0x1F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                } else if ((data[i] & 0xF0) == 0xE0) {
                    uc = (data[i] & 0x0F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;

                } else if ((data[i] & 0xF8) == 0xF0) {
                    uc = (data[i] & 0x07);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;

                } else if ((data[i] & 0xFC) == 0xF8) {
                    uc = (data[i] & 0x03);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;

                } else if ((data[i] & 0xFE) == 0xFC) {
                    uc = (data[i] & 0x01);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                    uc <<= 6;
                    uc |= (data[i] & 0x3F);
                    i++;
                }
                len = toChars(uc, chars, len);
            }
        }
        return new String(chars, 0, len);
    }

    public static int toChars(int codePoint, char[] dst, int index) {
        if (codePoint < 0 || codePoint > Character.MAX_CODE_POINT) {
            throw new IllegalArgumentException();
        }
        if (codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            dst[index] = (char) codePoint;
            return ++index;
        }
        int offset = codePoint - Character.MIN_SUPPLEMENTARY_CODE_POINT;
        dst[index + 1] = (char) ((offset & 0x3ff) + Character.MIN_LOW_SURROGATE);
        dst[index] = (char) ((offset >>> 10) + Character.MIN_HIGH_SURROGATE);
        return index + 2;
    }
}