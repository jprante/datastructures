package org.xbib.datastructures.json.iterator.test.output;

import org.xbib.datastructures.json.iterator.output.JsonStream;
import junit.framework.TestCase;

import java.math.BigInteger;

public class TestInteger extends TestCase {
    public void testBigInteger() {
        assertEquals("100", JsonStream.serialize(new BigInteger("100")));
    }
}
