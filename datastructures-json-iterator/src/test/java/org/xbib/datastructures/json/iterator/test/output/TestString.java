package org.xbib.datastructures.json.iterator.test.output;

import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.Config;
import junit.framework.TestCase;

public class TestString extends TestCase {
    public void test_unicode() {
        String output = JsonStream.serialize(new Config.Builder().escapeUnicode(false).build(), "中文");
        assertEquals("\"中文\"", output);
    }
    public void test_escape_control_character() {
        String output = JsonStream.serialize(new String(new byte[]{0}));
        assertEquals("\"\\u0000\"", output);
    }
}
