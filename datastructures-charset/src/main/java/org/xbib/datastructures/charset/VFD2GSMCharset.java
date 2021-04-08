package org.xbib.datastructures.charset;

import java.util.Arrays;

/**
 * The <b>VFD2GSMCharset</b> class is mostly based on standard GSM charset, but
 * just a few chars (@, German chars) use different byte values.
 *
 * The SMSC uses the German National changes Replacement Codes (NRCs, s. ISO 21
 * German) for the representation of the characters ä, Ä, ö, Ö, ü, Ü, ß, §.
 * Also, the @ char is encoded strange.
 *
 * NOTE: This charset was only for MT (application -> mobile).  Vodafone-D2 has
 * an entirely different charset for MO (mobile -> application).
 */
public class VFD2GSMCharset extends GSMCharset {
    
    private static final int GSM_COL = 0;
    private static final int VFD2_COL = 1;
    private static final byte[][] VFD2_OVERRIDE_TABLE = {
        { (byte)0x00, (byte)0x40 }, // @
        { (byte)0x02, (byte)0x24 }, // $
        { (byte)0x1E, (byte)0x7E }, // ß
        { (byte)0x24, (byte)0x02 }, // ¤
        { (byte)0x40, (byte)0xA1 }, // ¡
        { (byte)0x5D, (byte)0x5F }, // Ñ
        { (byte)0x5E, (byte)0x5D }, // Ü
        { (byte)0x5F, (byte)0x5E }, // §
        { (byte)0x7D, (byte)0x1E }, // ñ
        { (byte)0x7E, (byte)0x7D }, // ü
    };

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        byte[] gsmBytes = super.encode(stringBuilder);
        for (int i = 0; i < gsmBytes.length; i++) {
            for (byte[] bytes : VFD2_OVERRIDE_TABLE) {
                if (gsmBytes[i] == bytes[GSM_COL]) {
                    gsmBytes[i] = bytes[VFD2_COL];
                    break;
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
            for (byte[] value : VFD2_OVERRIDE_TABLE) {
                if (bytes[i] == value[VFD2_COL]) {
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
