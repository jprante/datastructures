package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class CharsetUtilTest {
    private static final Logger logger = Logger.getLogger(CharsetUtilTest.class.getName());

    @Test
    public void encode() throws Exception {
        // euro currency symbol
        StringBuilder str0 = new StringBuilder("\u20ac");
        byte[] bytes = null;

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B65"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_PACKED_GSM);
        assertArrayEquals(Hex.hexToByteArray("9B32"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2);
        assertArrayEquals(Hex.hexToByteArray("20AC"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16BE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2LE);
        assertArrayEquals(Hex.hexToByteArray("AC20"), bytes);
        assertArrayEquals(str0.toString().getBytes("UTF-16LE"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UTF_8);
        assertArrayEquals(Hex.hexToByteArray("E282AC"), bytes);
        assertArrayEquals(str0.toString().getBytes("UTF-8"), bytes);

        // latin-1 doesn't contain the euro symbol - replace with '?'
        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_1);
        assertArrayEquals(Hex.hexToByteArray("3F"), bytes);
        assertArrayEquals(str0.toString().getBytes("ISO-8859-1"), bytes);

        // latin-9 does contain the euro symbol
        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_15);
        assertArrayEquals(Hex.hexToByteArray("A4"), bytes);
        assertArrayEquals(str0.toString().getBytes("ISO-8859-15"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertArrayEquals(Hex.hexToByteArray("1B65"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFD2_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B65"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFTR_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B65"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertArrayEquals(Hex.hexToByteArray("80"), bytes);


        // longer string with @ symbol in-between
        str0 = new StringBuilder("Hello @ World");

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F200020576F726C64"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_PACKED_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("C8329BFD060140D7B79C4D06"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("00480065006C006C006F0020004000200057006F0072006C0064"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16BE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2LE);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("480065006C006C006F0020004000200057006F0072006C006400"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16LE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UTF_8);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F204020576F726C64"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_8), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_1);
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F204020576F726C64"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.ISO_8859_1), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_15);
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F204020576F726C64"), bytes);
        assertArrayEquals(str0.toString().getBytes("ISO-8859-15"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F200020576F726C64"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFD2_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F204020576F726C64"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFTR_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F204020576F726C64"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_TMOBILENL_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("48656C6C6F200020576F726C64"), bytes);


        // longer string with @ symbol in-between
        str0 = new StringBuilder("JoeyBlue");

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_PACKED_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("CA77392F64D7CB"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("004A006F006500790042006C00750065"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16BE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2LE);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("4A006F006500790042006C0075006500"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16LE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UTF_8);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_8), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_1);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.ISO_8859_1), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_15);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);
        assertArrayEquals(str0.toString().getBytes("ISO-8859-15"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFD2_GSM);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFTR_GSM);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertArrayEquals(Hex.hexToByteArray("4A6F6579426C7565"), bytes);


        // longer string with @ symbol in-between
        str0 = new StringBuilder("{}[]$");

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("1B281B291B3C1B3E02"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_PACKED_GSM);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("1BD426B5E16D7C02"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("007B007D005B005D0024"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16BE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UCS_2LE);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("7B007D005B005D002400"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_16LE), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_UTF_8);
        //logger.debug(HexUtil.toHexString(bytes));
        assertArrayEquals(Hex.hexToByteArray("7B7D5B5D24"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.UTF_8), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_1);
        assertArrayEquals(Hex.hexToByteArray("7B7D5B5D24"), bytes);
        assertArrayEquals(str0.toString().getBytes(StandardCharsets.ISO_8859_1), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_ISO_8859_15);
        assertArrayEquals(Hex.hexToByteArray("7B7D5B5D24"), bytes);
        assertArrayEquals(str0.toString().getBytes("ISO-8859-15"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertArrayEquals(Hex.hexToByteArray("1B281B291B3C1B3E02"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFD2_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B281B291B3C1B3E24"), bytes);

        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFTR_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B281B291B3C1B3E24"), bytes);

        // {}[] not supported
        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertArrayEquals(Hex.hexToByteArray("3F3F3F3F02"), bytes);

        // chars specifically to vodafone-turkey
        //str0 = "$@£¤¥§ÄÅßñΓΔΘΩ€";
        str0 = new StringBuilder("$@\u00a3\u00a4\u00a5\u00a7\u00c4\u00c5\u00df\u00f1\u0393\u0394\u0398\u03a9\u20ac");
        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_VFTR_GSM);
        assertArrayEquals(Hex.hexToByteArray("2440A3A4A5A7C4C5DFF1137F19151B65"), bytes);

        // form feed is an escape code in GSM
        str0 = new StringBuilder("\f\f");
        bytes = CharsetUtil.encode(str0, CharsetUtil.CHARSET_GSM);
        assertArrayEquals(Hex.hexToByteArray("1B0A1B0A"), bytes);
    }

    @Test
    public void decode() throws Exception {
        // euro currency symbol
        String str0 = "\u20ac";
        String str1 = null;

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B65"), CharsetUtil.CHARSET_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("9B32"), CharsetUtil.CHARSET_PACKED_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("20AC"), CharsetUtil.CHARSET_UCS_2);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("E282AC"), CharsetUtil.CHARSET_UTF_8);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B65"), CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertEquals(str0, str1);

        // latin-1 doesn't contain the euro symbol - replace with '?'
        //str1 = CharsetUtil.decode(Hex.hexToByteArray("3F"), CharsetUtil.CHARSET_ISO_8859_1);
        //assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("A4"), CharsetUtil.CHARSET_ISO_8859_15);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B65"), CharsetUtil.CHARSET_VFD2_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B65"), CharsetUtil.CHARSET_VFTR_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("80"), CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertEquals(str0, str1);


        // longer string with @ symbol in-between
        str0 = "Hello @ World";

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F200020576F726C64"), CharsetUtil.CHARSET_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("C8329BFD060140D7B79C4D06"), CharsetUtil.CHARSET_PACKED_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("00480065006C006C006F0020004000200057006F0072006C0064"), CharsetUtil.CHARSET_UCS_2);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_UTF_8);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_ISO_8859_1);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_ISO_8859_15);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_VFD2_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F204020576F726C64"), CharsetUtil.CHARSET_VFTR_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("48656C6C6F200020576F726C64"), CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertEquals(str0, str1);


        // longer string with @ symbol in-between
        str0 = "JoeyBlue";

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("CA77392F64D7CB"), CharsetUtil.CHARSET_PACKED_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("004A006F006500790042006C00750065"), CharsetUtil.CHARSET_UCS_2);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_UTF_8);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_ISO_8859_1);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_ISO_8859_15);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_VFD2_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_VFTR_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("4A6F6579426C7565"), CharsetUtil.CHARSET_TMOBILENL_GSM);
        assertEquals(str0, str1);


        // longer string with @ symbol in-between
        str0 = "{}[]$";

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B281B291B3C1B3E02"), CharsetUtil.CHARSET_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1BD426B5E16D7C02"), CharsetUtil.CHARSET_PACKED_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("007B007D005B005D0024"), CharsetUtil.CHARSET_UCS_2);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("7B7D5B5D24"), CharsetUtil.CHARSET_UTF_8);
        assertEquals(str0, str1);

        // airwide is close to GSM, $ is 0x24 rather than 0x02 though
        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B281B291B3C1B3E24"), CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("7B7D5B5D24"), CharsetUtil.CHARSET_ISO_8859_1);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("7B7D5B5D24"), CharsetUtil.CHARSET_ISO_8859_15);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B281B291B3C1B3E24"), CharsetUtil.CHARSET_VFD2_GSM);
        assertEquals(str0, str1);

        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B281B291B3C1B3E24"), CharsetUtil.CHARSET_VFTR_GSM);
        assertEquals(str0, str1);

        // skip TMOBILENL_GSM - can't encode {}[]

        // had problem passing these tests on linux vs. mac os x -- issue with
        // byte encoding on differnet platforms, replaced tests with source strings
        // that use Java unicode escapes
        // helpful URL: http://www.greywyvern.com/code/php/utf8_html
        // decode a string with every char in VFD2-GSM
        // str0 = "@$ß¤¡ÑÜ§ñü_";
        str0 = "@$\u00df\u00a4\u00a1\u00d1\u00dc\u00a7\u00f1\u00fc_";
        str1 = CharsetUtil.decode(Hex.hexToByteArray("40247E02A15F5D5E1E7D11"), CharsetUtil.CHARSET_VFD2_GSM);
        assertEquals(str0, str1);

        //str0 = "@$ß$@ÑÜ_ñü_";
        str0 = "@$\u00df$@\u00d1\u00dc_\u00f1\u00fc_";
        str1 = CharsetUtil.decode(Hex.hexToByteArray("40241E24405D5E5F7D7E5F"), CharsetUtil.CHARSET_AIRWIDE_IA5);
        assertEquals(str0, str1);

        // chars specifically to vodafone-turkey
        //str0 = "$@£¤¥§ÄÅßñΓΔΘΩ€";
        str0 = "$@\u00a3\u00a4\u00a5\u00a7\u00c4\u00c5\u00df\u00f1\u0393\u0394\u0398\u03a9\u20ac";
        str1 = CharsetUtil.decode(Hex.hexToByteArray("2440A3A4A5A7C4C5DFF1137F19151B65"), CharsetUtil.CHARSET_VFTR_GSM);
        assertEquals(str0, str1);

        // form feed GSM escape sequence
        str0 = "\f\f";
        str1 = CharsetUtil.decode(Hex.hexToByteArray("1B0A1B0A"), CharsetUtil.CHARSET_GSM);
        assertEquals(str0, str1);
    }

    @Test
    public void verifyDecodeDoesNotChangeByteArray() throws Exception {
        for (Map.Entry<String, Charset> entry : CharsetUtil.getCharsetMap().entrySet()) {
            byte[] bytes = new byte[] { (byte)0x40, (byte)0x5F, (byte)0x24, (byte)0x78, (byte)0x02, (byte)0x02};
            byte[] expectedBytes = Arrays.copyOf(bytes, bytes.length);
            String str0 = CharsetUtil.decode(bytes, entry.getValue());
            // test that the byte array wasn't changed
            assertArrayEquals(expectedBytes, bytes, "Charset " + entry.getKey() + " impl bad -- modified byte array parameter");
        }
    }

    @Test
    public void verifyNullByteArray() throws Exception {
        for (Map.Entry<String,Charset> entry : CharsetUtil.getCharsetMap().entrySet()) {
            // test that the byte array wasn't changed
            assertNull(CharsetUtil.decode(null, entry.getValue()), "Charset " + entry.getKey() + " impl bad -- did not return null");
        }
    }

    @Test
    public void decodeToStringBuilderAllCharsets() throws Exception {
        // try every charset with simple A-Z, a-z, and 0-9 which should work in all charsets
        StringBuilder expectedString = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz01234567890");
        // test decode to stringBuilder
        for (Map.Entry<String,Charset> entry : CharsetUtil.getCharsetMap().entrySet()) {
            // make this a harder test where we actually test this was appended!
            byte[] expectedBytes = CharsetUtil.encode(expectedString, entry.getKey());
            StringBuilder sb = new StringBuilder("T");
            CharsetUtil.decode(expectedBytes, sb, entry.getValue());
            assertEquals("Charset " + entry.getKey() + " impl broken", "T"+expectedString, sb.toString());
        }
    }

    @Test
    public void decodeToStringAllCharsets() {
        // try every charset with simple A-Z, a-z, and 0-9 which should work in all charsets
        StringBuilder expectedString = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz01234567890");
        // test decode to stringBuilder
        for (Map.Entry<String,Charset> entry : CharsetUtil.getCharsetMap().entrySet()) {
            // make this a harder test where we actually test this was appended!
            byte[] expectedBytes = CharsetUtil.encode(expectedString, entry.getValue());
            String actualString = CharsetUtil.decode(expectedBytes, entry.getKey());
            assertEquals(expectedString.toString(), actualString, "Charset " + entry.getKey() + " impl broken");
        }
    }

    @Test
    public void normalize() throws Exception {
        StringBuilder in = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz01234567890?&@");
        for (Map.Entry<String,Charset> entry : CharsetUtil.getCharsetMap().entrySet()) {
            assertEquals(in.toString(), CharsetUtil.normalize(in, entry.getValue()), "Charset " + entry.getKey() + " implementation broken");
        }
        in = new StringBuilder("\u20AC");  // euro currency char (only supported in a couple charsets)
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_GSM));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_PACKED_GSM));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_AIRWIDE_GSM));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_VFD2_GSM));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_VFTR_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_ISO_8859_1));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_ISO_8859_15));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UCS_2));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UCS_2LE));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UTF_8));
        assertEquals("\u20AC", CharsetUtil.normalize(in, CharsetUtil.CHARSET_TMOBILENL_GSM));

        in = new StringBuilder("\u6025");  // arabic char (only supported in a couple charsets)
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_PACKED_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_AIRWIDE_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_VFD2_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_VFTR_GSM));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_ISO_8859_1));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_ISO_8859_15));
        assertEquals("\u6025", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UCS_2));
        assertEquals("\u6025", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UCS_2LE));
        assertEquals("\u6025", CharsetUtil.normalize(in, CharsetUtil.CHARSET_UTF_8));
        assertEquals("?", CharsetUtil.normalize(in, CharsetUtil.CHARSET_TMOBILENL_GSM));
    }
}
