package org.xbib.datastructures.json.iterator.test.extra;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.extra.JdkDatetimeSupport;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import junit.framework.TestCase;

import java.util.Date;


public class TestJdkDatetime extends TestCase {

    public void skip_test() {
        JdkDatetimeSupport.enable("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        assertEquals("\"1970-01-01T08:00:00.000+0800\"", JsonStream.serialize(new Date(0)));
        Date obj = JsonIterator.deserialize("\"1970-01-01T08:00:00.000+0800\"", Date.class);
        assertEquals(0, obj.getTime());
    }
}
