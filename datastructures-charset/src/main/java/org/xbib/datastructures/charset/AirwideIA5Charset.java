package org.xbib.datastructures.charset;

import java.util.Arrays;

/**
 * The <b>AirwideIA5Charset</b> class handles a charset unique to the "Airwide"
 * SMSC vendor.  Airwide basically took IA5 and GSM and then decided to treat just
 * a few characters different than other characters.  The result is that some
 * byte values will need transcoded first, then run thru the normal GSMCharset
 * class.  Stupid, stupid Airwide....
 * 
 */
public class AirwideIA5Charset extends GSMCharset {

    private static final int GSM_COL = 0;
    private static final int AIRWIDE_COL = 1;
    private static final byte[][] AIRWIDE_OVERRIDE_TABLE = {
        { (byte)0x00, (byte)0x40 }, // @
        { (byte)0x02, (byte)0x24 }, // $
        { (byte)0x11, (byte)0x5F }, // _
    };

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        // encoding of unicode to "AIRWIDE-GSM" is the exact same
        return super.encode(stringBuilder);
    }

    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
        int length = (bytes == null ? 0 : bytes.length);

        // we promise to not change any of the bytes -- an optimization is a
        // lazy "copy" of the byte array in case we don't encounter any bytes
        // that need to be converted
        byte[] bytes2 = null;

        // decoding "AIRWIDE-GSM" to unicode is nearly same process as "GSM", but
        // a few byte values need to be converted -- we'll first convert from AIRWIDE-GSM to GSM
        // a little slow to run thru this again, but SMS are so tiny its not worth optimizing
        for (int i = 0; i < length; i++) {
            for (byte[] value : AIRWIDE_OVERRIDE_TABLE) {
                // if we find a AIRWIDE-GSM byte value in our AIRWIDE column
                // we need to swap its value with a replacement value
                if (bytes[i] == value[AIRWIDE_COL]) {
                    // we found a special byte value, check if we need to copy
                    // the byte array now for a "lazy" copy
                    if (bytes2 == null) {
                        bytes2 = Arrays.copyOf(bytes, bytes.length);
                    }
                    bytes2[i] = value[GSM_COL];
                    // need to immediately exit search so that we don't run into
                    // the bug where 0x00 -> 0x40 and then 0x40 -> 0xA1
                    break;
                }
            }
        }
        // delegate to parent (pick which byte array is correct)
        super.decode((bytes2 == null ? bytes : bytes2), buffer);
    }

}
