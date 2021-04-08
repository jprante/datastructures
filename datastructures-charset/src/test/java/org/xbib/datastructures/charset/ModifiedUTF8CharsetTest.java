package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ModifiedUTF8CharsetTest {

    private static final Logger logger = Logger.getLogger(ModifiedUTF8CharsetTest.class.getName());

    String nullString = "\u0000";
    String controlCharsString = createStringWithCharRange('\u0001', 0x20);
    String asciiOnlyString = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    String iso88591CharsString = createStringWithCharRange('\u0080', 128);
    String first7EFFString = createStringWithCharRange('\u0100', 0x7EFF);
    String entireString = createStringWithCharRange('\u0000', 0x7FFF);
    String upperRangeString = createStringWithCharRange('\u7FFF', 0x8000);
    
    static public String createStringWithCharRange(char start, int length) {
        StringBuilder buf = new StringBuilder(length);
        int end = start+length;
        for (int i = start; i < end; i++) {
            buf.append((char)i);
        }
        return buf.toString();
    }
    
    @Test
    public void compareAgainstJVM() throws Exception {
        byte[] expected = null;
        byte[] actual = null;
        String actualString = null;
        
        String[] strings = new String[] {
            nullString, controlCharsString, asciiOnlyString, iso88591CharsString, first7EFFString, entireString
        };
        
        int i = 0;
        for (String s : strings) {
            expected = s.getBytes(StandardCharsets.UTF_8);
            actual = CharsetUtil.CHARSET_MODIFIED_UTF8.encode(new StringBuilder(s));
            //logger.info("  string: " + s);
            //logger.info("expected: " + HexUtil.toHexString(expected));
            //logger.info("  actual: " + HexUtil.toHexString(actual));
            // verify our length calculator is correct
            assertEquals(expected.length, ModifiedUTF8Charset.calculateByteLength(s));
            assertArrayEquals(expected, actual, "string: " + s);
            // try to decode the byte array and make sure it matches the expected string
            actualString = CharsetUtil.CHARSET_MODIFIED_UTF8.decode(expected);
            assertEquals(s, actualString);
            // verify a decode to a stringbuffer works as expected
            StringBuilder actualStringBuffer = new StringBuilder();
            CharsetUtil.decode(expected, actualStringBuffer, CharsetUtil.CHARSET_MODIFIED_UTF8);
            assertEquals(s, actualStringBuffer.toString());
            i++;
        }
        
        // upper range of java values are where modified UTF-8 falls on its face
        // its still safe to use as long as modified UTF-8 bytes are used to decode
        // the values as well -- verify the entire range decodes back to the same values
        byte[] encoded = CharsetUtil.CHARSET_MODIFIED_UTF8.encode(new StringBuilder(upperRangeString));
        String decoded = CharsetUtil.decode(encoded, CharsetUtil.CHARSET_MODIFIED_UTF8);
        assertEquals(upperRangeString, decoded);
    }
    
    @Test
    public void calculateByteLength() throws Exception {
        String sample = null;
        // test the incredibly fast method for calculating a Java strings UTF-8 byte length
        assertEquals(0, ModifiedUTF8Charset.calculateByteLength(null));
        assertEquals(0, ModifiedUTF8Charset.calculateByteLength(""));
        assertEquals(1, ModifiedUTF8Charset.calculateByteLength("a"));
        assertEquals(2, ModifiedUTF8Charset.calculateByteLength("\n\r"));
        sample = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(sample.getBytes(StandardCharsets.UTF_8).length, ModifiedUTF8Charset.calculateByteLength(sample));
        sample = "\u20ac";
        assertEquals(sample.getBytes(StandardCharsets.UTF_8).length, ModifiedUTF8Charset.calculateByteLength(sample));
        sample = "\u20ac\u0623";
        assertEquals(sample.getBytes(StandardCharsets.UTF_8).length, ModifiedUTF8Charset.calculateByteLength(sample));
        sample = "\u00A7\u00E5\uFFFF";
        assertEquals(sample.getBytes(StandardCharsets.UTF_8).length, ModifiedUTF8Charset.calculateByteLength(sample));
    }
    
    @Test
    public void emoticons() throws Exception {
        // follows sample of unit test in for UTF8Charset
        // these chars triggered a problem in production -- these are specifically
        // not supported for decoding -- but should work to/from for serialization
        // U+1F631 is a very high range example of an emoticon (something more people are using)
        // UTF-8 bytes look like this: F09F98B1
        // UTF-16 bytes look like this: D83DDE31
        // JavaScript escapes: \uD83D\uDE31
        byte[] bytes = Hex.hexToByteArray("F09F98B1");
        String str = "\uD83D\uDE31";    // this is the UTF-16 version of the UTF-8 bytes
        
        try {
            String t = CharsetUtil.CHARSET_MODIFIED_UTF8.decode(bytes);
            fail("exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // correct behavior -- this UTF-8 char is NOT supported!
        }
        
        // try serializing and deserializing
        byte[] encoded = CharsetUtil.CHARSET_MODIFIED_UTF8.encode(new StringBuilder(str));
        // this is what the Modified UTF-8 version looks like: EDA0BDEDB8B1     // 6 bytes instead of 4
        //logger.info(HexUtil.toHexString(encoded));
        String decoded = CharsetUtil.CHARSET_MODIFIED_UTF8.decode(encoded);
        
        assertEquals(str, decoded);
    }
}
