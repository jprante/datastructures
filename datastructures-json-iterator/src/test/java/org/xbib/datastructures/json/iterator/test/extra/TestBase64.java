package org.xbib.datastructures.json.iterator.test.extra;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.extra.Base64Support;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import junit.framework.TestCase;

public class TestBase64 extends TestCase {
    static {
        Base64Support.enable();
    }

    public void test_encode() {
        assertEquals("\"YWJj\"", JsonStream.serialize("abc".getBytes()));
    }

    public void test_decode() {
        assertEquals("abc", new String(JsonIterator.deserialize("\"YWJj\"", byte[].class)));
    }
}
