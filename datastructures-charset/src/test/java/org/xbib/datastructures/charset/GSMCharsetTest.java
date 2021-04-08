package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

public class GSMCharsetTest {

    private static final Logger logger = Logger.getLogger(GSMCharsetTest.class.getName());

    @Test
    public void canRepresent() throws Exception {
        // nulls are always ok
        assertTrue(GSMCharset.canRepresent(null));
        assertTrue(GSMCharset.canRepresent(" "));
        assertTrue(GSMCharset.canRepresent("\n\r"));
        assertTrue(GSMCharset.canRepresent("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(GSMCharset.canRepresent("Hello @ World"));
        assertTrue(GSMCharset.canRepresent("{}[]$"));
        // euro currency symbol is good
        assertTrue(GSMCharset.canRepresent("\u20ac"));
        // arabic char is not valid GSM char
        assertFalse(GSMCharset.canRepresent("\u0623"));
        // bug found with A-z if statement in previous charset
        // 1 char in-between the upper-case and lower-case snuck in the
        // simple range check -- the '`' char is NOT in the GSM charset
        assertFalse(GSMCharset.canRepresent("`"));
        assertTrue(GSMCharset.canRepresent("[\\]^_"));
        // form feed: uncommon but technically part of the spec
        assertTrue(GSMCharset.canRepresent("\f"));
        
        // create a fully correct string from lookup tables
        // strings to decode/encode to/from UTF-8
        // build a string of every GSM char
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < GSMCharset.CHAR_TABLE.length; i++) {
            char c = GSMCharset.CHAR_TABLE[i];
            if (c > 0) {
                s.append(c);
            }
        }
        for (int i = 0; i < GSMCharset.EXT_CHAR_TABLE.length; i++) {
            char c = GSMCharset.EXT_CHAR_TABLE[i];
            if (c > 0) {
                s.append(c);
            }
        }
        assertTrue(GSMCharset.canRepresent(s.toString()));
    }
}
