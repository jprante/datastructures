package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

public class TMobileNlGSMCharsetTest {

    private static final Logger logger = Logger.getLogger(TMobileNlGSMCharsetTest.class.getName());

    @Test
    public void canRepresent() throws Exception {
        // nulls are always ok
        assertTrue(TMobileNlGSMCharset.canRepresent(null));
        assertTrue(TMobileNlGSMCharset.canRepresent(" "));
        assertTrue(TMobileNlGSMCharset.canRepresent("\n\r"));
        assertTrue(TMobileNlGSMCharset.canRepresent("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(TMobileNlGSMCharset.canRepresent("Hello @ World"));
        assertTrue(TMobileNlGSMCharset.canRepresent("$_"));
        // euro currency symbol is good
        assertTrue(TMobileNlGSMCharset.canRepresent("\u20ac"));
        // arabic char is not valid TMobileNlGSM char
        assertFalse(TMobileNlGSMCharset.canRepresent("\u0623"));
        // '`' char is NOT in the TMobileNlGSM charset
        assertFalse(TMobileNlGSMCharset.canRepresent("`"));
        // []{}^~|\ GSM extended table chars are not supported by T-Mo NL
        assertFalse(TMobileNlGSMCharset.canRepresent("{}[\\]^~|"));
        
        // create a fully correct string from lookup table
        // strings to decode/encode to/from UTF-8
        // build a string of every GSM base-table char
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < GSMCharset.CHAR_TABLE.length; i++) {
            char c = GSMCharset.CHAR_TABLE[i];
            if (c > 0) {
                s.append(c);
            }
        }
        s.append("\u20ac");  // and the euro-mark

        assertTrue(TMobileNlGSMCharset.canRepresent(s.toString()));
    }

    @Test
    public void testEncodeDecode() throws Exception {
        
        TMobileNlGSMCharset tmo = new TMobileNlGSMCharset();

        // test custom euro encode/decode
        StringBuilder customEuro = new StringBuilder("\u20ac");
        byte[] bytes = tmo.encode(customEuro);
        assertEquals(1, bytes.length);
        assertEquals(0x80, bytes[0] & 0x0ff);
        StringBuilder sb = new StringBuilder();
        tmo.decode(bytes, sb);
        assertEquals(1, sb.length());
        assertEquals(customEuro.toString(), sb.toString());

        // validate custom euro hex-encodes & decodes correctly also
        String hexEncoded = new String(Hex.byteArraryToHex(bytes));
        assertEquals("80", hexEncoded);
        byte[] bytes2 = Hex.hexToByteArray(hexEncoded);
        sb = new StringBuilder();
        tmo.decode(bytes2, sb);
        assertEquals(1, sb.length());
        assertEquals(customEuro.toString(), sb.toString());

        // test invalid GSM chars; all should encode to '?'
        StringBuilder invalidChars = new StringBuilder("[]{}|^\\~");
        bytes = tmo.encode(invalidChars);
        for (byte b : bytes) {
            assertEquals(0x3f, b);
        }
        // arabic char is not valid GSM char
        StringBuilder otherUnicode = new StringBuilder("\u0623");
        bytes = tmo.encode(otherUnicode);
        assertEquals(1, bytes.length);
        assertEquals(0x3f, bytes[0]);
    }
}
