package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

public class MobileTextUtilTest {

    private static final Logger logger = Logger.getLogger(MobileTextUtilTest.class.getName());

    @Test
    public void replaceSafeUnicodeChars() {
        String source = null;
        StringBuilder buffer = null;
        int replaced = -1;

        source = "hello";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceSafeUnicodeChars(buffer);
        assertEquals(0, replaced);
        assertEquals(source, buffer.toString());


        source = "\u201chello\u201d \u201cworld\u201d \u201cthis\u201d";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceSafeUnicodeChars(buffer);
        assertEquals(6, replaced);
        assertEquals("\"hello\" \"world\" \"this\"", buffer.toString());


        source = "\u201chello\u201d \u201cworld\u201d \u201cthis\u201d";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceSafeUnicodeChars(buffer);
        assertEquals(6, replaced);
        assertEquals("\"hello\" \"world\" \"this\"", buffer.toString());


        source = "\u2018hello\u2019 \u2018world\u2019 don\u2019t";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceSafeUnicodeChars(buffer);
        assertEquals(5, replaced);
        assertEquals("\'hello\' \'world\' don\'t", buffer.toString());
    }

    @Test
    public void replaceAccentedChars() throws Exception {
        String source = null;
        StringBuilder buffer = null;
        int replaced = -1;

        source = "hello";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceAccentedChars(buffer);
        assertEquals(0, replaced);
        assertEquals(source, buffer.toString());

        source = "h\u00E9llo";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceAccentedChars(buffer);
        assertEquals(1, replaced);
        assertEquals("hello", buffer.toString());

        source = "\u00E8\u00E9\u00EA\u00EB\u00EF\u00F1\u00F2";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceAccentedChars(buffer);
        assertEquals(7, replaced);
        assertEquals("eeeeino", buffer.toString());

        source = "\u20AC";
        buffer = new StringBuilder(source);
        replaced = MobileTextUtil.replaceAccentedChars(buffer);
        assertEquals(0, replaced);
        assertEquals("\u20AC", buffer.toString());
    }
}
