package org.xbib.datastructures.charset;

import java.util.Arrays;

/**
 * The <b>VFTRGSMCharset</b> class is mostly based on Latin-1, but mixed almost
 * equally with the GSM default charset.
 */
public class VFTRGSMCharset extends GSMCharset {
    
    private static final int GSM_COL = 0;
    private static final int VFTR_COL = 1;
    private static final byte[][] VFTR_OVERRIDE_TABLE = {
        { (byte)0x02, (byte)0x24 }, // $
        { (byte)0x00, (byte)0x40 }, // @
        { (byte)0x11, (byte)0x5F }, // _
        { (byte)0x40, (byte)0xA1 }, // ¡
        { (byte)0x01, (byte)0xA3 }, // £
        { (byte)0x24, (byte)0xA4 }, // ¤
        { (byte)0x03, (byte)0xA5 }, // ¥
        { (byte)0x5F, (byte)0xA7 }, // §
        { (byte)0x60, (byte)0xBF }, // ¿
        { (byte)0x5B, (byte)0xC4 }, // Ä
        { (byte)0x0E, (byte)0xC5 }, // Å
        { (byte)0x1C, (byte)0xC6 }, // Æ
        { (byte)0x09, (byte)0xC7 }, // Ç
        { (byte)0x1F, (byte)0xC9 }, // É
        { (byte)0x5D, (byte)0xD1 }, // Ñ
        { (byte)0x5C, (byte)0xD6 }, // Ö
        { (byte)0x0B, (byte)0xD8 }, // Ø
        { (byte)0x5E, (byte)0xDC }, // Ü
        { (byte)0x1E, (byte)0xDF }, // ß
        { (byte)0x7F, (byte)0xE0 }, // à
        { (byte)0x7B, (byte)0xE4 }, // ä
        { (byte)0x0F, (byte)0xE5 }, // å
        { (byte)0x1D, (byte)0xE6 }, // æ
        { (byte)0x04, (byte)0xE8 }, // è
        { (byte)0x05, (byte)0xE9 }, // é
        { (byte)0x07, (byte)0xEC }, // ì
        { (byte)0x7D, (byte)0xF1 }, // ñ
        { (byte)0x08, (byte)0xF2 }, // ò
        { (byte)0x7C, (byte)0xF6 }, // ö
        { (byte)0x0C, (byte)0xF8 }, // ø
        { (byte)0x06, (byte)0xF9 }, // ù
        { (byte)0x7E, (byte)0xFC }, // ü
        { (byte)0x10, (byte)0x7F }, // Δ
    };

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        byte[] gsmBytes = super.encode(stringBuilder);
        for (int i = 0; i < gsmBytes.length; i++) {
            for (byte[] bytes : VFTR_OVERRIDE_TABLE) {
                if (gsmBytes[i] == bytes[GSM_COL]) {
                    gsmBytes[i] = bytes[VFTR_COL];
                }
            }
        }
        return gsmBytes;
    }

    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
        int length = (bytes == null ? 0 : bytes.length);
        byte[] bytes2 = null;
        for (int i = 0; i < length; i++) {
            for (byte[] value : VFTR_OVERRIDE_TABLE) {
                if (bytes[i] == value[VFTR_COL]) {
                    if (bytes2 == null) {
                        bytes2 = Arrays.copyOf(bytes, bytes.length);
                    }
                    bytes2[i] = value[GSM_COL];
                    break;
                }
            }
        }
        super.decode((bytes2 == null ? bytes : bytes2), buffer);
    }
}
