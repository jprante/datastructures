package org.xbib.datastructures.json.iterator.test;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.ValueType;
import junit.framework.TestCase;

import java.io.IOException;

public class TestWhatIsNext extends TestCase {
    public void test() throws IOException {
        JsonIterator parser = JsonIterator.parse("{}");
        assertEquals(ValueType.OBJECT, parser.whatIsNext());
    }
}
